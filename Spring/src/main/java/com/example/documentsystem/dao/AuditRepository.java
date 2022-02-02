package com.example.documentsystem.dao;

import com.example.documentsystem.entities.AuditEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuditRepository extends JpaRepository<AuditEntity, Long> {
    List<AuditEntity> removeByDocumentId(Long documentId);
    List<AuditEntity> findAllByDocumentId(Long documentId);
}
