package com.example.documentsystem.services;

import com.example.documentsystem.entities.AuditEntity;

import java.util.List;

public interface AuditService {
    List<AuditEntity> findAll();

    AuditEntity findById(Long id);

    List<AuditEntity> findByDocumentId(Long documentId);

    AuditEntity create(AuditEntity audit);

    AuditEntity update(AuditEntity audit);

    AuditEntity deleteById(Long id);
}
