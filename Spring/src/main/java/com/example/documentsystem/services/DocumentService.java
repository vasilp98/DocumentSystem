package com.example.documentsystem.services;

import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.StoredDocument;
import com.example.documentsystem.models.ViewingDocumentBundle;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    List<DocumentEntity> findAll();

    DocumentEntity findById(Long id);

    ViewingDocumentBundle getDocumentForViewing(Long id);

    Document create(StoredDocument document, MultipartFile file);

    DocumentEntity update(DocumentEntity document);

    DocumentEntity deleteById(Long id);
}
