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

    @GetMapping
    public List<PermissionDto> getAll() {
        return permissionService.findAll();
    }

    @GetMapping("/{permissionId}")
    public PermissionDto getPermission(@PathVariable Long permissionId) {
        return permissionService.findById(permissionId);
    }

    @PostMapping
    public PermissionDto createPermissions(@RequestBody PermissionDto permissionDto) {
        return permissionService.create(permissionDto);
    }

    @PostMapping("/{permissionId}/users")
    public PermissionDto updatePermission(@PathVariable Long permissionId, @RequestBody List<Long> userIds) {
        return permissionService.update(permissionId, userIds);
    }
}
