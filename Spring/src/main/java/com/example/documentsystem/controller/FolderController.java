package com.example.documentsystem.controller;

import com.example.documentsystem.entities.FolderEntity;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.FolderDto;
import com.example.documentsystem.models.permission.Permission;
import com.example.documentsystem.models.permission.PermissionDto;
import com.example.documentsystem.services.DocumentService;
import com.example.documentsystem.services.FolderService;
import com.example.documentsystem.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/folder")
public class FolderController {
    private FolderService folderService;
    private DocumentService documentService;
    private PermissionService permissionService;

    @Autowired
    public FolderController(
            FolderService folderService,
            DocumentService documentService,
            PermissionService permissionService) {
        this.folderService = folderService;
        this.documentService = documentService;
        this.permissionService = permissionService;
    }

    @GetMapping("/{folderId}")
    public FolderEntity findById(@PathVariable Long folderId){
        return folderService.findById(folderId);
    }

    @GetMapping
    public List<FolderEntity> getAll() {
        return folderService.findAll();
    }

    @GetMapping("/{folderId}/documents")
    public List<Document> findDocumentsByFolderId(@PathVariable Long folderId){
        return documentService.findAllInFolder(folderId);
    }

    @PostMapping
    public FolderEntity createFolder(@Valid @RequestBody FolderDto folderDto) {
        return folderService.create(folderDto);
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Long> delete(@PathVariable Long folderId) {
        Long id = folderService.deleteById(folderId).getId();
        return new ResponseEntity<>(id, HttpStatus.OK);
    }
}
