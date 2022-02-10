package com.example.documentsystem.services.impl;

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
public class PermissionServiceImpl implements PermissionService {
    private PermissionRepository permissionRepository;
    private UserRepository userRepository;
    private FilterService filterService;

    public PermissionServiceImpl(
            PermissionRepository permissionRepository,
            UserRepository userRepository,
            FilterService filterService) {

        this.permissionRepository = permissionRepository;
        this.userRepository = userRepository;
        this.filterService = filterService;
    }

    @Override
    public void checkFolderPermission(Long folderId, Permission permission) {
        if (!hasFolderPermission(folderId, permission))
            throw new PermissionException("You don't have permission to execute this action");
    }

    @Override
    public boolean hasFolderPermission(Long folderId, Permission permission) {
        String username = Context.getCurrentUserName();
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("User with username=%s not found or you don't have permissions to access it.", username)));

        return userEntity.getPermissions()
                .stream().anyMatch(p -> p.getArea() == PermissionArea.FOLDER &&
                        p.getFolderId() == folderId &&
                        p.getPermissions().contains(permission));
    }

    @Override
    public void checkDocumentPermission(DocumentEntity documentEntity, Permission permission) {
        if (hasDocumentPermission(documentEntity, permission))
            throw new PermissionException("You don't have permission to execute this action");
    }

    @Override
    public boolean hasDocumentPermission(DocumentEntity documentEntity, Permission permission) {
        String username = Context.getCurrentUserName();
        UserEntity userEntity = userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("User with username=%s not found or you don't have permissions to access it.", username)));

        List<PermissionEntity> documentPermissions = userEntity.getPermissions()
                .stream().filter(p -> p.getArea() == PermissionArea.DOCUMENT)
                .collect(Collectors.toList());

        for (PermissionEntity permissionEntity : documentPermissions) {
            if (filterService.checkDocument(documentEntity, permissionEntity.getFilter().deserializeToFilters()))
                return true;
        }

        return false;
    }

    @Override
    public List<PermissionDto> findAllInFolder(Long folderId) {
        return permissionRepository.findAllByFolderId(folderId).stream().map(pe -> pe.toDto()).collect(Collectors.toList());
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
                permissionDto.getArea(),
                permissionDto.getPermissions(),
                permissionDto.getFilters().serialize())).toDto();
    }

    @Override
    public PermissionDto update(PermissionDto permissionDto) {
        PermissionEntity permissionEntity = permissionRepository.getById(permissionDto.getId());

        if (permissionDto.getPermissions() != null)
            permissionEntity.setPermissions(permissionDto.getPermissions());

        if (permissionDto.getFilters() != null)
            permissionEntity.setFilter(permissionDto.getFilters().serialize());

        permissionRepository.save(permissionEntity);

        if (permissionDto.getUserIds() != null) {
            for (Long userId: permissionDto.getUserIds()) {
                UserEntity userEntity = userRepository.getById(userId);
                userEntity.getPermissions().add(permissionEntity);
                userRepository.save(userEntity);
            }
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
