package com.example.documentsystem.services;

import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.DocumentUserFields;
import com.example.documentsystem.models.StoredDocument;
import com.example.documentsystem.models.ViewingDocumentBundle;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface DocumentService {
    List<Document> findAllInFolder(Long folderId);

    Document findById(Long id);

    ViewingDocumentBundle getDocumentForViewing(Long id);

    Document create(StoredDocument document, MultipartFile file);

    Document updateFields(Long id, DocumentUserFields fields);

    void addFile(Long documentId, MultipartFile file);

    InputStream getFile(Long documentId, Integer fileNumber);

    Document deleteById(Long id);
}
