package com.example.documentsystem.dao;

import com.example.documentsystem.entities.DocumentLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DocumentLinkRepository extends JpaRepository<DocumentLinkEntity, Long> {
    Optional<DocumentLinkEntity> findByTokenAndPassword(String token, String password);
}
