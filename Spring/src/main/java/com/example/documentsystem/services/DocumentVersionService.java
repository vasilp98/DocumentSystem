package com.example.documentsystem.services;

import com.example.documentsystem.entities.DocumentVersionEntity;

import java.util.List;

public interface DocumentVersionService {
    List<DocumentVersionEntity> findAll();

    DocumentVersionEntity findById(Long id);

    DocumentVersionEntity create(DocumentVersionEntity version);

    DocumentVersionEntity update(DocumentVersionEntity version);

    DocumentVersionEntity deleteById(Long id);
}
