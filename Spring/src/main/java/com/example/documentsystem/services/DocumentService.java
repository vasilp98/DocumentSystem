package com.example.documentsystem.services;

import com.example.documentsystem.entities.DocumentEntity;

import java.util.List;

public interface DocumentService {
    List<DocumentEntity> findAll();

    DocumentEntity findById(Long id);

    DocumentEntity create(DocumentEntity document);

    DocumentEntity update(DocumentEntity document);

    DocumentEntity deleteById(Long id);
}
