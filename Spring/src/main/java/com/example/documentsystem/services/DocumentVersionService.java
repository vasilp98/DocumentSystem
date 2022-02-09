package com.example.documentsystem.services;

import com.example.documentsystem.entities.DocumentVersionEntity;
import com.example.documentsystem.models.Document;

import java.io.InputStream;
import java.util.List;

public interface DocumentVersionService {
    List<Document> findAllByDocumentId(Long documentId);

    Document findById(Long id);

    InputStream getFile(Long documentId, Integer fileNumber);

    Document create(Long documentId);

    Document deleteById(Long id);
}
