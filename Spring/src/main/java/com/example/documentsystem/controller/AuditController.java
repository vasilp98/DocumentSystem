package com.example.documentsystem.controller;

import com.example.documentsystem.models.auditing.AuditEvent;
import com.example.documentsystem.services.AuditingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {
    private AuditingService auditingService;

    @Autowired
    public AuditController(AuditingService auditingService) {
        this.auditingService = auditingService;
    }

    @GetMapping("/{documentId}")
    public List<AuditEvent> findByDocumentId(@PathVariable Long documentId){
        return auditingService.getEventsForDocument(documentId);
    }
}
