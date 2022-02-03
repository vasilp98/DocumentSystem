package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.DocumentLinkRepository;
import com.example.documentsystem.entities.DocumentLinkEntity;
import com.example.documentsystem.exceptions.UnauthorizedException;
import com.example.documentsystem.models.DocumentLink;
import com.example.documentsystem.services.DocumentLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@Transactional
public class DocumentLinkServiceImpl implements DocumentLinkService {
    private final DocumentLinkRepository documentLinkRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public DocumentLinkServiceImpl(DocumentLinkRepository documentLinkRepository, PasswordEncoder passwordEncoder) {
        this.documentLinkRepository = documentLinkRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Long getDocumentIdFromLink(String token, String password) {
        String encryptedPassword = passwordEncoder.encode(password);
        DocumentLinkEntity documentLinkEntity = documentLinkRepository.findByTokenAndPassword(token, encryptedPassword)
                .orElseThrow(() -> new UnauthorizedException("Wrong password for document link"));

        if (documentLinkEntity.getValidUntil() != null && documentLinkEntity.getValidUntil().isBefore(LocalDateTime.now())) {
            documentLinkRepository.delete(documentLinkEntity);
            throw new EntityNotFoundException("Document link not found");
        }

        return documentLinkEntity.getDocumentId();
    }

    @Override
    public DocumentLinkEntity create(DocumentLink link) {
        DocumentLinkEntity documentLinkEntity = new DocumentLinkEntity(
                UUID.randomUUID().toString(),
                link.getDocumentId(),
                link.getPassword(),
                link.getValidUntil());

        return documentLinkRepository.save(documentLinkEntity);
    }
}
