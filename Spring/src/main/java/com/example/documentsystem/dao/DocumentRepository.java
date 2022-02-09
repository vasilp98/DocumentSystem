package com.example.documentsystem.dao;

import com.example.documentsystem.entities.DocumentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {
    List<DocumentEntity> findByFolderIdOrderById(Long folderId);

    List<DocumentEntity> findAllByCurrentDocumentIdOrderByVersionNumberDesc(Long currentDocumentId);
}
