package com.example.documentsystem.services;


import com.example.documentsystem.entities.FolderEntity;
import com.example.documentsystem.models.FolderDto;

import java.util.List;

public interface FolderService {
    List<FolderEntity> findAll();

    FolderEntity findById(Long id);

    FolderEntity create(FolderDto folderDto);

    FolderEntity update(Long folderId, String name);

    FolderEntity deleteById(Long id);
}
