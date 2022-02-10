package com.example.documentsystem.models.permission;

import com.example.documentsystem.models.filter.Filter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class PermissionDto {
    private Long id;

    private Long folderId;

    private List<Long> userIds;

    private PermissionArea area;

    private List<Permission> permissions;

    private List<Filter> filters;
}
