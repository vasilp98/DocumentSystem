package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.DocumentRepository;
import com.example.documentsystem.dao.DocumentVersionRepository;
import com.example.documentsystem.dao.FileRepository;
import com.example.documentsystem.dao.content.FileContentId;
import com.example.documentsystem.dao.content.FileContentRepository;
import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.entities.DocumentVersionEntity;
import com.example.documentsystem.entities.FileEntity;
import com.example.documentsystem.exceptions.UploadFileException;
import com.example.documentsystem.extensions.EntityExtensions;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.auditing.AuditEventType;
import com.example.documentsystem.services.AuditingService;
import com.example.documentsystem.services.DocumentVersionService;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@ExtensionMethod(EntityExtensions.class)
public class DocumentVersionServiceImpl implements DocumentVersionService {
    private final DocumentRepository documentRepository;
    private FileRepository fileRepository;
    private FileContentRepository fileContentRepository;
    private AuditingService auditingService;

    @Autowired
    public DocumentVersionServiceImpl(DocumentRepository documentRepository,
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
    public List<Document> findAllByDocumentId(Long documentId) {
        return documentRepository.findAllByCurrentDocumentIdOrderByVersionNumberDesc(documentId)
                .stream().filter(de -> de.getId() != de.getCurrentDocumentId()).map(de -> de.toDocument()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Document findById(Long versionId) {
        return documentRepository.findById(versionId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Version with ID=%s not found.", versionId))).toDocument();
    }

    @Override
    public InputStream getFile(Long versionId, Integer fileNumber) {
        FileEntity firstFileEntity = fileRepository.findByDocumentIdAndNumber(versionId, fileNumber);

        InputStream firstFileStream = fileContentRepository.retrieve(new FileContentId(versionId, firstFileEntity.getId()));

        return firstFileStream;
    }

    @Override
    public Document create(Long documentId) {
        String currentUser = getCurrentUserName();

        DocumentEntity currentDocumentEntity = documentRepository.getById(documentId);
        DocumentEntity versionEntity = new DocumentEntity(currentDocumentEntity);

        currentDocumentEntity.setVersionNumber(currentDocumentEntity.getVersionNumber() + 1);
        documentRepository.save(currentDocumentEntity);

        versionEntity.setModifyUser(currentUser);
        versionEntity.setModifyDate(LocalDateTime.now());
        versionEntity = documentRepository.save(versionEntity);

        auditingService.auditEvent(AuditEventType.CREATE_VERSION, documentId);

        List<FileEntity> fileEntities = fileRepository.findAllByDocumentId(documentId);
        for (int i = 0; i < fileEntities.size(); i++) {
            FileEntity currentFileEntity = fileEntities.get(i);

            FileEntity fileEntity = new FileEntity(currentFileEntity);
            fileEntity.setDocumentId(versionEntity.getId());
            fileEntity = fileRepository.save(fileEntity);

            InputStream inputStream = fileContentRepository.retrieve(new FileContentId(documentId, currentFileEntity.getId()));

            fileContentRepository.add(new FileContentId(versionEntity.getId(), fileEntity.getId()), inputStream);
        }

        return versionEntity.toDocument();
    }

    @Override
    public Document deleteById(Long versionId) {
        Document oldDocument = findById(versionId);
        documentRepository.deleteById(versionId);
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
