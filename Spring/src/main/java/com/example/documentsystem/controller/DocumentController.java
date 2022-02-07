package com.example.documentsystem.controller;

import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.DocumentUserFields;
import com.example.documentsystem.models.StoredDocument;
import com.example.documentsystem.models.ViewingDocumentBundle;
import com.example.documentsystem.models.auditing.AuditEvent;
import com.example.documentsystem.services.AuditingService;
import com.example.documentsystem.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/document")
public class DocumentController {
    private DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/{documentId}")
    public ViewingDocumentBundle findByDocumentId(@PathVariable Long documentId){
        return documentService.getDocumentForViewing(documentId);
    }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Document upload(@RequestPart StoredDocument document, @RequestPart MultipartFile file) {
        return documentService.create(document, file);
    }

    @PostMapping("/{documentId}/fields")
    public Document updateFields(@PathVariable Long documentId, @RequestBody DocumentUserFields fields) {
        return documentService.updateFields(documentId, fields);
    }

    @PostMapping("/{documentId}/files")
    public void addFile(@PathVariable Long documentId, @RequestParam("file") MultipartFile file) {
        documentService.addFile(documentId, file);
    }
}
