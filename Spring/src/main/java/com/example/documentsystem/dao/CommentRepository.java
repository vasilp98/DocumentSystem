package com.example.documentsystem.dao;

import com.example.documentsystem.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findAllByDocumentIdOrderByCreatedAsc(Long documentId);
}
