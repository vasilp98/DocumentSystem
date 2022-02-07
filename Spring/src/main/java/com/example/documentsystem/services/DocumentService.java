package com.example.documentsystem.services;

import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.DocumentUserFields;
import com.example.documentsystem.models.StoredDocument;
import com.example.documentsystem.models.ViewingDocumentBundle;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface DocumentService {
    List<Document> findAllInFolder(Long folderId);

    Document findById(Long id);

    ViewingDocumentBundle getDocumentForViewing(Long id);

    Document create(StoredDocument document, MultipartFile file);

    Document updateFields(Long id, DocumentUserFields fields);

    void addFile(Long documentId, MultipartFile file);

    Document deleteById(Long id);
}
