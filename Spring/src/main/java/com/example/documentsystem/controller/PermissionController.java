package com.example.documentsystem.controller;

import com.example.documentsystem.models.ListDto;
import com.example.documentsystem.models.permission.PermissionDto;
import com.example.documentsystem.services.PermissionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permission")
public class PermissionController {
    private PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @GetMapping("/{folderId}")
    public List<PermissionDto> getAllInFolder(@PathVariable Long folderId) {
        return permissionService.findAllInFolder(folderId);
    }

    @GetMapping("/{listId}")
    public PermissionDto getList(@PathVariable Long permissionId) {
        return permissionService.findById(permissionId);
    }

    @PostMapping
    public PermissionDto createList(@RequestBody PermissionDto permissionDto) {
        return permissionService.create(permissionDto);
    }

    @PostMapping
    public PermissionDto createPermissions(@RequestBody PermissionDto permissionDto) {
        return permissionService.create(permissionDto);
    }

    @PostMapping("/{permissionId}")
    public PermissionDto updatePermission(@PathVariable Long permissionId, @RequestBody PermissionDto permissionDto) {
        return permissionService.update(permissionDto);
    }
}
