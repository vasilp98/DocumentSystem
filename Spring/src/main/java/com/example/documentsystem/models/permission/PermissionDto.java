package com.example.documentsystem.models.permission;

import com.example.documentsystem.models.filter.Filter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PermissionDto {
    private Long id;

    private Long folderId;

    private String folderName;

    private String name;

    private List<Long> userIds;

    private PermissionArea area;

    private List<Permission> permissions;

    private Filter filter;

    public PermissionDto(Long id, Long folderId, String name,
                         List<Long> userIds, PermissionArea area, List<Permission> permissions, Filter filter) {
        this.id = id;
        this.folderId = folderId;
        this.name = name;
        this.userIds = userIds;
        this.area = area;
        this.permissions = permissions;
        this.filter = filter;
    }
}
