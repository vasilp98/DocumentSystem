package com.example.documentsystem.controller;

import com.example.documentsystem.entities.DocumentLinkEntity;
import com.example.documentsystem.models.DocumentLink;
import com.example.documentsystem.models.DocumentLinkPassword;
import com.example.documentsystem.models.ViewingDocumentBundle;
import com.example.documentsystem.services.DocumentLinkService;
import com.example.documentsystem.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;

@RestController
@RequestMapping("/api/link")
public class DocumentLinkController {
    private DocumentLinkService documentLinkService;
    private DocumentService documentService;

    @Autowired
    public DocumentLinkController(DocumentLinkService documentLinkService, DocumentService documentService) {
        this.documentLinkService = documentLinkService;
        this.documentService = documentService;
    }

    @PostMapping("/{token}")
    public InputStream downloadFormLink(@PathVariable String token, @RequestBody DocumentLinkPassword linkPassword) {
        Long documentId = documentLinkService.getDocumentIdFromLink(token, linkPassword.getPassword());
        ViewingDocumentBundle documentBundle = documentService.getDocumentForViewing(documentId);

        return documentBundle.getFirstFileStream();
    }

    @PostMapping
    public DocumentLinkEntity createLink(@RequestBody DocumentLink documentLink) {
        return documentLinkService.create(documentLink);
    }
}
