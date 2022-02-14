package com.example.documentsystem.services;

import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.models.permission.Permission;
import com.example.documentsystem.models.permission.PermissionArea;
import com.example.documentsystem.models.permission.PermissionDto;

import java.util.List;

public interface PermissionService {
    void checkFolderPermission(Long folderId, Permission permission);

    boolean hasFolderPermission(Long folderId, Permission permission);

    void checkDocumentPermission(DocumentEntity documentEntity, Permission permission);

    boolean hasDocumentPermission(DocumentEntity documentEntity, Permission permission);

    List<PermissionDto> findAll();

    PermissionDto findById(Long id);

    PermissionDto create(PermissionDto permissionDto);

    PermissionDto update(PermissionDto permissionDto);

    PermissionDto deleteById(Long id);
}
