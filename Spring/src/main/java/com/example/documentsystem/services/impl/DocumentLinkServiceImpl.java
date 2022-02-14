package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.DocumentLinkRepository;
import com.example.documentsystem.dao.DocumentRepository;
import com.example.documentsystem.entities.DocumentLinkEntity;
import com.example.documentsystem.exceptions.UnauthorizedException;
import com.example.documentsystem.extensions.EntityExtensions;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.DocumentLink;
import com.example.documentsystem.models.auditing.AuditEventType;
import com.example.documentsystem.services.AuditingService;
import com.example.documentsystem.services.DocumentLinkService;
import com.example.documentsystem.services.DocumentService;
import lombok.experimental.ExtensionMethod;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
@ExtensionMethod(EntityExtensions.class)
public class DocumentLinkServiceImpl implements DocumentLinkService {
    private final DocumentLinkRepository documentLinkRepository;
    private AuditingService auditingService;
    private PasswordEncoder passwordEncoder;
    private DocumentRepository documentRepository;

    @Autowired
    public DocumentLinkServiceImpl(
            DocumentRepository documentRepository,
            DocumentLinkRepository documentLinkRepository,
            AuditingService auditingService,
            PasswordEncoder passwordEncoder) {
        this.documentLinkRepository = documentLinkRepository;
        this.auditingService = auditingService;
        this.passwordEncoder = passwordEncoder;
        this.documentRepository = documentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Document getDocumentFromLink(String token, String password) {
        DocumentLinkEntity documentLinkEntity = documentLinkRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Wrong password for document link"));

        if (!passwordEncoder.matches(password, documentLinkEntity.getPassword()))
            throw new UnauthorizedException("Wrong password for document link");

        if (documentLinkEntity.getValidUntil() != null && documentLinkEntity.getValidUntil().isBefore(LocalDate.now())) {
            documentLinkRepository.delete(documentLinkEntity);
            throw new EntityNotFoundException("Document link not found");
        }

        return documentRepository.getById(documentLinkEntity.getDocumentId()).toDocument();
    }

    @Override
    public DocumentLinkEntity create(DocumentLink link) {
        String encryptedPassword = passwordEncoder.encode(link.getPassword());

        DocumentLinkEntity documentLinkEntity = new DocumentLinkEntity(
                UUID.randomUUID().toString(),
                link.getDocumentId(),
                encryptedPassword,
                link.getValidUntil());

        auditingService.auditEvent(AuditEventType.CREATE_LINK, link.getDocumentId());
        return documentLinkRepository.save(documentLinkEntity);
    }
}
