package com.example.documentsystem.dao;

import com.example.documentsystem.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, Long> {
    FileEntity findByDocumentIdAndNumber(Long documentId, Integer number);

    List<FileEntity> findAllByDocumentId(Long documentId);
}
