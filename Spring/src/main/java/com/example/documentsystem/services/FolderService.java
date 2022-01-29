package com.example.documentsystem.services;


import com.example.documentsystem.entities.FolderEntity;

import java.util.List;

public interface FolderService {
    List<FolderEntity> findAll();

    FolderEntity findById(Long id);

    FolderEntity create(FolderEntity folder);

    FolderEntity update(FolderEntity folder);

    FolderEntity deleteById(Long id);
}
