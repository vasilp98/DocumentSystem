package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.FolderRepository;
import com.example.documentsystem.dao.PermissionRepository;
import com.example.documentsystem.dao.UserRepository;
import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.entities.PermissionEntity;
import com.example.documentsystem.entities.UserEntity;
import com.example.documentsystem.exceptions.PermissionException;
import com.example.documentsystem.extensions.EntityExtensions;
import com.example.documentsystem.models.permission.Permission;
import com.example.documentsystem.models.permission.PermissionArea;
import com.example.documentsystem.models.permission.PermissionDto;
import com.example.documentsystem.services.FilterService;
import com.example.documentsystem.services.FolderService;
import com.example.documentsystem.services.PermissionService;
import com.example.documentsystem.services.context.Context;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@ExtensionMethod(EntityExtensions.class)
public class AllPermissionsServiceImpl implements PermissionService {
    private PermissionRepository permissionRepository;
    private UserRepository userRepository;
    private FilterService filterService;
    private FolderRepository folderRepository;

    public AllPermissionsServiceImpl(
            PermissionRepository permissionRepository,
            UserRepository userRepository,
            FilterService filterService,
            FolderRepository folderRepository) {

        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.filterService = filterService;
        this.folderRepository = folderRepository;
    }

    @Override
    public void checkFolderPermission(Long folderId, Permission permission) {
        return;
    }

    @Override
    public boolean hasFolderPermission(Long folderId, Permission permission) {
        return true;
    }

    @Override
    public void checkDocumentPermission(DocumentEntity documentEntity, Permission permission) {
        return;
    }

    @Override
    public boolean hasDocumentPermission(DocumentEntity documentEntity, Permission permission) {
        return true;
    }

    @Override
    public List<PermissionDto> findAll() {
        List<PermissionEntity> permissionEntities = permissionRepository.findAll();

        List<PermissionDto> permissionDtos = permissionEntities.stream().map(pe -> pe.toDto()).collect(Collectors.toList());

        permissionDtos.stream().forEach(p -> p.setFolderName(folderRepository.getById(p.getFolderId()).getName()));
        return permissionDtos;
    }

    @Override
    public PermissionDto findById(Long id) {
        return permissionRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Permission with ID=%s not found.", id))).toDto();
    }

    @Override
    public PermissionDto create(PermissionDto permissionDto) {
        return permissionRepository.save(new PermissionEntity(
                permissionDto.getFolderId(),
                permissionDto.getName(),
                permissionDto.getArea(),
                permissionDto.getPermissions(),
                permissionDto.getFilters().serialize())).toDto();
    }

    @Override
    public PermissionDto update(Long permissionId, List<Long> userIds) {
        PermissionEntity permissionEntity = permissionRepository.getById(permissionId);

        for (UserEntity userEntity: permissionEntity.getUsers()) {
            userEntity.getPermissions().remove(permissionEntity);
            userRepository.save(userEntity);
        }

        for (Long userId: userIds) {
            UserEntity userEntity = userRepository.getById(userId);
            userEntity.getPermissions().add(permissionEntity);
            userRepository.save(userEntity);
        }

        return permissionEntity.toDto();
    }

    @Override
    public PermissionDto deleteById(Long id) {
        PermissionDto old = findById(id);
        permissionRepository.deleteById(id);
        return old;
    }
}
