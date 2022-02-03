package com.example.documentsystem.controller;

import com.example.documentsystem.models.ViewingDocumentBundle;
import com.example.documentsystem.models.auditing.AuditEvent;
import com.example.documentsystem.services.AuditingService;
import com.example.documentsystem.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
