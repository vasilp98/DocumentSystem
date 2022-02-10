package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.FolderRepository;
import com.example.documentsystem.entities.FolderEntity;
import com.example.documentsystem.models.permission.Permission;
import com.example.documentsystem.services.FolderService;
import com.example.documentsystem.services.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;
    private PermissionService permissionService;

    @Autowired
    public FolderServiceImpl(FolderRepository folderRepository, PermissionService permissionService) {
        this.folderRepository = folderRepository;
        this.permissionService = permissionService;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FolderEntity> findAll() {
        return folderRepository.findAll().stream()
                .filter(p -> permissionService.hasFolderPermission(p.getId(), Permission.READ)).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public FolderEntity findById(Long folderId) {
        permissionService.checkFolderPermission(folderId, Permission.READ);

        return folderRepository.findById(folderId)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Folder with ID=%s not found.", folderId)));
    }

    @Override
    public FolderEntity create(FolderEntity folder) {
        return folderRepository.save(folder);
    }

    @Override
    public FolderEntity update(FolderEntity folder) {
        permissionService.checkFolderPermission(folder.getId(), Permission.WRITE);

        return folderRepository.save(folder);
    }

    @Override
    public FolderEntity deleteById(Long folderId) {
        permissionService.checkFolderPermission(folderId, Permission.DELETE);

        FolderEntity old = findById(folderId);
        folderRepository.deleteById(folderId);
        return old;
    }
}
