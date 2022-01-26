package com.example.documentsystem.dao;

import com.example.documentsystem.entities.DocumentVersionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentVersionRepository extends JpaRepository<DocumentVersionEntity, Long> {
}
