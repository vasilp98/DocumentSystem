package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.DocumentRepository;
import com.example.documentsystem.dao.FileRepository;
import com.example.documentsystem.dao.content.FileContentId;
import com.example.documentsystem.dao.content.FileContentRepository;
import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.entities.FileEntity;
import com.example.documentsystem.exceptions.UploadFileException;
import com.example.documentsystem.extensions.EntityExtensions;
import com.example.documentsystem.models.*;
import com.example.documentsystem.models.auditing.AuditEventType;
import com.example.documentsystem.models.permission.Permission;
import com.example.documentsystem.services.AuditingService;
import com.example.documentsystem.services.DocumentService;
import com.example.documentsystem.services.PermissionService;
import com.example.documentsystem.services.context.Context;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
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
    private PermissionService permissionService;

    @Autowired
    public DocumentServiceImpl(
            DocumentRepository documentRepository,
            FileRepository fileRepository,
            FileContentRepository fileContentRepository,
            AuditingService auditingService,
            PermissionService permissionService) {

        this.documentRepository = documentRepository;
        this.fileRepository = fileRepository;
        this.fileContentRepository = fileContentRepository;
        this.auditingService = auditingService;
        this.permissionService = permissionService;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<Document> findAllInFolder(Long folderId)
    {
        return documentRepository.findByFolderIdOrderById(folderId)
                .stream().filter(d -> permissionService.hasDocumentPermission(d, Permission.READ) &&
                        d.getId() == d.getCurrentDocumentId())
                .map(de -> de.toDocument()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Document findById(Long documentId) {
        DocumentEntity documentEntity = findEntityById(documentId);
        permissionService.checkDocumentPermission(documentEntity, Permission.READ);

        return documentEntity.toDocument();
    }

    @Override
    @Transactional(readOnly = true)
    public ViewingDocumentBundle getDocumentForViewing(Long id) {
        Document document = findById(id);
        FileEntity firstFileEntity = fileRepository.findByDocumentIdAndNumber(id, 0);

        InputStream firstFileStream = fileContentRepository.retrieve(new FileContentId(id, firstFileEntity.getId()));
        return new ViewingDocumentBundle(firstFileStream, document);
    }

    @Override
    public Document create(StoredDocument storedDocument) {
        permissionService.checkFolderPermission(storedDocument.getFolderId(), Permission.WRITE);

        String currentUser = Context.getCurrentUserName();
        DocumentEntity documentEntity = new DocumentEntity(
                storedDocument.getFolderId(),
                currentUser,
                currentUser,
                0L,
                0,
                storedDocument.getUserFields()
        );

        documentEntity = documentRepository.save(documentEntity);

        documentEntity.setCurrentDocumentId(documentEntity.getId());
        documentEntity.setVersionNumber(1);
        documentEntity = documentRepository.save(documentEntity);

        auditingService.auditEvent(AuditEventType.STORE, documentEntity.getId());

        return documentEntity.toDocument();
    }

    @Override
    public Document updateFields(Long documentId, DocumentUserFields fields) {
        DocumentEntity documentEntity = findEntityById(documentId);
        permissionService.checkDocumentPermission(documentEntity, Permission.WRITE);

        documentEntity.setModifyDate(LocalDate.now());
        documentEntity.setModifyUser(Context.getCurrentUserName());

        //User fields
        documentEntity.setName(fields.getName());
        documentEntity.setDocumentType(fields.getDocumentType());
        documentEntity.setCompany(fields.getCompany());
        documentEntity.setDate(fields.getDate());
        documentEntity.setContact(fields.getContact());
        documentEntity.setStatus(fields.getStatus());
        documentEntity.setAmount(fields.getAmount());
        documentEntity.setNumber(fields.getNumber());

        documentRepository.save(documentEntity);

        auditingService.auditEvent(AuditEventType.UPDATE_FIELDS, documentId);
        return documentEntity.toDocument();
    }

    @Override
    public void addFile(Long documentId, MultipartFile file) {
        DocumentEntity documentEntity = findEntityById(documentId);
        permissionService.checkDocumentPermission(documentEntity, Permission.WRITE);

        documentEntity.setModifyDate(LocalDate.now());
        documentEntity.setModifyUser(Context.getCurrentUserName());

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

    @Override
    @Transactional(readOnly = true)
    public FileStream getFile(Long documentId, Integer fileNumber) {
        FileEntity firstFileEntity = fileRepository.findByDocumentIdAndNumber(documentId, fileNumber);

        InputStream firstFileStream = fileContentRepository.retrieve(new FileContentId(documentId, firstFileEntity.getId()));

        return new FileStream(firstFileStream, firstFileEntity.getOriginalFileName());
    }

    @Override
    public List<FileEntity> getFiles(Long documentId) {
        auditingService.auditEvent(AuditEventType.OPEN, documentId);
        return fileRepository.findAllByDocumentId(documentId);
    }

    @Override
    public Document deleteById(Long documentId) {
        DocumentEntity documentEntity = findEntityById(documentId);
        permissionService.checkDocumentPermission(documentEntity, Permission.DELETE);

        documentRepository.deleteById(documentId);

        auditingService.auditEvent(AuditEventType.DELETE, documentId);
        return documentEntity.toDocument();
    }

    private DocumentEntity findEntityById(Long documentId) {
        return documentRepository.findById(documentId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Document with ID=%s not found.", documentId)));
    }
}
