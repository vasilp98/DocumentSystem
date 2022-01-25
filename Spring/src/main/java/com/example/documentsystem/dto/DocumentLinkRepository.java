package com.example.documentsystem.dto;

import com.example.documentsystem.entities.DocumentLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DocumentLinkRepository extends JpaRepository<DocumentLinkEntity, Long> {
}
