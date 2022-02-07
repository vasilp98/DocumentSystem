package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.DocumentRepository;
import com.example.documentsystem.dao.FileRepository;
import com.example.documentsystem.dao.content.FileContentId;
import com.example.documentsystem.dao.content.FileContentRepository;
import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.entities.FileEntity;
import com.example.documentsystem.exceptions.UploadFileException;
import com.example.documentsystem.extensions.EntityExtensions;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.DocumentUserFields;
import com.example.documentsystem.models.StoredDocument;
import com.example.documentsystem.models.ViewingDocumentBundle;
import com.example.documentsystem.models.auditing.AuditEventType;
import com.example.documentsystem.models.auditing.AuditedField;
import com.example.documentsystem.services.AuditingService;
import com.example.documentsystem.services.DocumentService;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@ExtensionMethod(EntityExtensions.class)
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final FileRepository fileRepository;
    private FileContentRepository fileContentRepository;
    private AuditingService auditingService;

    @Autowired
    public DocumentServiceImpl(
            DocumentRepository documentRepository,
            FileRepository fileRepository,
            FileContentRepository fileContentRepository,
            AuditingService auditingService) {

        this.documentRepository = documentRepository;
        this.fileRepository = fileRepository;
        this.fileContentRepository = fileContentRepository;
        this.auditingService = auditingService;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Document> findAllInFolder(Long folderId)
    {
        return documentRepository.findByFolderIdOrderById(folderId)
                .stream().map(de -> de.toDocument()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Document findById(Long documentId) {
        DocumentEntity documentEntity = documentRepository.findById(documentId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Document with ID=%s not found.", documentId)));

        return documentEntity.toDocument();
    }

    @Override
    @Transactional(readOnly = true)
    public ViewingDocumentBundle getDocumentForViewing(Long id) {
        Document document = findById(id);
        FileEntity firstFileEntity = fileRepository.findByDocumentIdAndNumber(id, 0L);

        InputStream firstFileStream = fileContentRepository.retrieve(new FileContentId(id, firstFileEntity.getId()));
        return new ViewingDocumentBundle(firstFileStream, document);
    }

    @Override
    public Document create(StoredDocument storedDocument, MultipartFile file) {
        String currentUser = getCurrentUserName();
        DocumentEntity documentEntity = new DocumentEntity(
                storedDocument.getFolderId(),
                storedDocument.getUserFields().getName(),
                currentUser,
                currentUser,
                file.getSize(),
                1
        );

        documentEntity = documentRepository.save(documentEntity);

        String extension = file.getOriginalFilename().split("\\.")[1];
        FileEntity fileEntity = new FileEntity(documentEntity.getId(), 0, file.getOriginalFilename(), extension, file.getSize());
        fileEntity = fileRepository.save(fileEntity);

        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException exception) {
            throw new UploadFileException(exception.getMessage());
        }

        auditingService.auditStore(new ArrayList<>(), documentEntity.getId());

        fileContentRepository.add(new FileContentId(documentEntity.getId(), fileEntity.getId()), inputStream);

        return documentEntity.toDocument();
    }

    @Override
    public Document updateFields(Long documentId, DocumentUserFields fields) {
        String currentUser = getCurrentUserName();

        DocumentEntity documentEntity = documentRepository.getById(documentId);
        documentEntity.setModifyDate(LocalDateTime.now());
        documentEntity.setModifyUser(currentUser);

        //TODO Set user fields to entity
        documentEntity.setName(fields.getName());

        documentRepository.save(documentEntity);

        auditingService.auditUpdateFields(getAuditedFields(documentEntity, fields), documentId);
        return documentEntity.toDocument();
    }

    @Override
    public void addFile(Long documentId, MultipartFile file) {
        String currentUser = getCurrentUserName();

        DocumentEntity documentEntity = documentRepository.getById(documentId);
        documentEntity.setModifyDate(LocalDateTime.now());
        documentEntity.setModifyUser(currentUser);

        Integer fileNumber = documentEntity.getFilesCount();
        documentEntity.setFilesCount(fileNumber + 1);
        documentEntity.setSize(documentEntity.getSize() + file.getSize());

        String extension = file.getOriginalFilename().split("\\.")[1];
        FileEntity fileEntity = new FileEntity(documentEntity.getId(), fileNumber, file.getOriginalFilename(), extension, file.getSize());
        fileEntity = fileRepository.save(fileEntity);

        InputStream inputStream;
        try {
            inputStream = file.getInputStream();
        } catch (IOException exception) {
            throw new UploadFileException(exception.getMessage());
        }

        auditingService.auditEvent(AuditEventType.CHANGE_CONTENT, documentEntity.getId());

        fileContentRepository.add(new FileContentId(documentEntity.getId(), fileEntity.getId()), inputStream);
    }

    private List<AuditedField> getAuditedFields(DocumentEntity currentDocumentEntity, DocumentUserFields updatedFields) {
        return new ArrayList<>();
    }

    @Override
    public Document deleteById(Long documentId) {
        Document oldDocument = findById(documentId);
        documentRepository.deleteById(documentId);

        auditingService.auditEvent(AuditEventType.DELETE, documentId);
        return oldDocument;
    }

    private String getCurrentUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }
}
