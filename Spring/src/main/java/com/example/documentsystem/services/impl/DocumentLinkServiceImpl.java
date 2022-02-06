package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.DocumentLinkRepository;
import com.example.documentsystem.entities.DocumentLinkEntity;
import com.example.documentsystem.exceptions.UnauthorizedException;
import com.example.documentsystem.models.DocumentLink;
import com.example.documentsystem.services.DocumentLinkService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@Slf4j
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
        DocumentLinkEntity documentLinkEntity = documentLinkRepository.findByToken(token)
                .orElseThrow(() -> new UnauthorizedException("Wrong password for document link"));

        if (!passwordEncoder.matches(password, documentLinkEntity.getPassword()))
            throw new UnauthorizedException("Wrong password for document link");

        if (documentLinkEntity.getValidUntil() != null && documentLinkEntity.getValidUntil().isBefore(LocalDateTime.now())) {
            documentLinkRepository.delete(documentLinkEntity);
            throw new EntityNotFoundException("Document link not found");
        }

        return documentLinkEntity.getDocumentId();
    }

    @Override
    public DocumentLinkEntity create(DocumentLink link) {
        String encryptedPassword = passwordEncoder.encode(link.getPassword());

        DocumentLinkEntity documentLinkEntity = new DocumentLinkEntity(
                UUID.randomUUID().toString(),
                link.getDocumentId(),
                encryptedPassword,
                link.getValidUntil());

        return documentLinkRepository.save(documentLinkEntity);
    }
}
