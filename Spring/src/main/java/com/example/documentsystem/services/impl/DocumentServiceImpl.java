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
import com.example.documentsystem.models.StoredDocument;
import com.example.documentsystem.models.ViewingDocumentBundle;
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
    public List<DocumentEntity> findAll() {
        return documentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentEntity findById(Long documentId) {
        return documentRepository.findById(documentId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Document with ID=%s not found.", documentId)));
    }

    @Override
    @Transactional(readOnly = true)
    public ViewingDocumentBundle getDocumentForViewing(Long id) {
        DocumentEntity documentEntity = findById(id);
        FileEntity firstFileEntity = fileRepository.findByDocumentIdAndNumber(id, 0L);

        InputStream firstFileStream = fileContentRepository.retrieve(new FileContentId(id, firstFileEntity.getId()));
        return new ViewingDocumentBundle(firstFileStream, documentEntity.toDocument());
    }

    @Override
    public Document create(StoredDocument storedDocument, MultipartFile file) {
        String currentUser = getCurrentUserName();
        DocumentEntity documentEntity = new DocumentEntity(
                storedDocument.getFolderId(),
                storedDocument.getName(),
                currentUser,
                currentUser,
                file.getSize(),
                1
        );

        documentEntity = documentRepository.save(documentEntity);

        String extension = file.getOriginalFilename().split("\\.")[1];
        FileEntity fileEntity = new FileEntity(documentEntity.getId(), 0L, file.getOriginalFilename(), extension, file.getSize());
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
    public DocumentEntity update(DocumentEntity document) {
        return documentRepository.save(document);
    }

    @Override
    public DocumentEntity deleteById(Long documentId) {
        DocumentEntity old = findById(documentId);
        documentRepository.deleteById(documentId);
        return old;
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
