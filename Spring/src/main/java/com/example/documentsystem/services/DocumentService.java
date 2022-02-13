package com.example.documentsystem.services;

import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.entities.FileEntity;
import com.example.documentsystem.models.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public interface DocumentService {
    List<Document> findAllInFolder(Long folderId);

    Document findById(Long id);

    ViewingDocumentBundle getDocumentForViewing(Long id);

    Document create(StoredDocument document);

    Document updateFields(Long id, DocumentUserFields fields);

    void addFile(Long documentId, MultipartFile file);

    FileStream getFile(Long documentId, Integer fileNumber);

    List<FileEntity> getFiles(Long documentId);

    Document deleteById(Long id);
}
