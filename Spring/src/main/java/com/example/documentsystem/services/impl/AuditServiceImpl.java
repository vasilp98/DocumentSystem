package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.AuditRepository;
import com.example.documentsystem.entities.AuditEntity;
import com.example.documentsystem.services.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class AuditServiceImpl implements AuditService {
    private final AuditRepository repo;

    @Autowired
    public AuditServiceImpl(AuditRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditEntity> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditEntity> findByDocumentId(Long documentId) {
        return repo.findAllByDocumentId(documentId);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditEntity findById(Long auditId) {
        return repo.findById(auditId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Audit with ID=%s not found.", auditId)));
    }

    @Override
    public AuditEntity create(AuditEntity audit) {
        return repo.save(audit);
    }

    @Override
    public AuditEntity update(AuditEntity audit) {
        return repo.save(audit);
    }

    @Override
    public AuditEntity deleteById(Long auditId) {
        AuditEntity old = findById(auditId);
        repo.deleteById(auditId);
        return old;
    }

}
