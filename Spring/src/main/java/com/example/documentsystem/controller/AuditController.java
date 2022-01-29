package com.example.documentsystem.controller;

import com.example.documentsystem.entities.AuditEntity;
import com.example.documentsystem.services.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/audit")
public class AuditController {
    @Autowired
    private AuditService auditService;

    @GetMapping("/{documentId}")
    public List<AuditEntity> findByDocumentId(@PathVariable Long documentId){
        return auditService.findByDocumentId(documentId);
    }
}
