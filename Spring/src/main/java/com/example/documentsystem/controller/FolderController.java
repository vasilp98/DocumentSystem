package com.example.documentsystem.controller;

import com.example.documentsystem.entities.FolderEntity;
import com.example.documentsystem.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/folder")
public class FolderController {
    private FolderService folderService;

    @Autowired
    public FolderController(FolderService folderService) {
        this.folderService = folderService;
    }

    @GetMapping("/{folderId}")
    public FolderEntity findById(@PathVariable Long folderId){
        return folderService.findById(folderId);
    }

    @GetMapping
    public List<FolderEntity> getAll() {
        return folderService.findAll();
    }
}
