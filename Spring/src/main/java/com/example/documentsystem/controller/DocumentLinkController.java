package com.example.documentsystem.controller;

import com.example.documentsystem.entities.DocumentLinkEntity;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.DocumentLink;
import com.example.documentsystem.models.DocumentLinkPassword;
import com.example.documentsystem.models.ViewingDocumentBundle;
import com.example.documentsystem.services.DocumentLinkService;
import com.example.documentsystem.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public Document getDocumentId(@PathVariable String token, @Valid @RequestBody DocumentLinkPassword linkPassword) {
        return documentLinkService.getDocumentFromLink(token, linkPassword.getPassword());
    }

    @PostMapping
    public DocumentLinkEntity createLink(@Valid @RequestBody DocumentLink documentLink) {
        return documentLinkService.create(documentLink);
    }
}
