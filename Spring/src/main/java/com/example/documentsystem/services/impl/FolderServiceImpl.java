package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.FolderRepository;
import com.example.documentsystem.entities.FolderEntity;
import com.example.documentsystem.entities.UserEntity;
import com.example.documentsystem.models.FolderDto;
import com.example.documentsystem.models.permission.Permission;
import com.example.documentsystem.services.FolderService;
import com.example.documentsystem.services.PermissionService;
import com.example.documentsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class FolderServiceImpl implements FolderService {
    private final FolderRepository folderRepository;
    private PermissionService permissionService;
    private UserService userService;

    @Autowired
    public FolderServiceImpl(
            FolderRepository folderRepository,
            PermissionService permissionService,
            UserService userService) {
        this.folderRepository = folderRepository;
        this.permissionService = permissionService;
        this.userService = userService;
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
    public FolderEntity create(FolderDto folderDto) {
        UserEntity currentUser = userService.getCurrentUser();

        FolderEntity folderEntity = new FolderEntity(
                currentUser.getId(),
                folderDto.getName(),
                LocalDateTime.now(),
                folderDto.getStorageLocation()
        );
        return folderRepository.save(folderEntity);
    }

    @Override
    public FolderEntity update(Long folderId, String name) {
        permissionService.checkFolderPermission(folderId, Permission.WRITE);

        FolderEntity folderEntity = folderRepository.getById(folderId);
        folderEntity.setName(name);

        return folderRepository.save(folderEntity);
    }

    @Override
    public FolderEntity deleteById(Long folderId) {
        permissionService.checkFolderPermission(folderId, Permission.DELETE);

        FolderEntity old = findById(folderId);
        folderRepository.deleteById(folderId);
        return old;
    }
}
