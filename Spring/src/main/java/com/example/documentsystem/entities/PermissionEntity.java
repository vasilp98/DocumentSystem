package com.example.documentsystem.entities;

import com.example.documentsystem.models.permission.Permission;
import com.example.documentsystem.models.permission.PermissionArea;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Data
@NoArgsConstructor
public class PermissionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ToString.Exclude
    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    private Set<UserEntity> users = new HashSet<>();

    private Long folderId;

    @Enumerated(EnumType.STRING)
    private PermissionArea area;

    @ElementCollection
    @JoinTable(name = "permission_right", joinColumns = @JoinColumn(name = "PERMISSION_ID"))
    @Column(name = "permission", nullable = false)
    @Enumerated(EnumType.STRING)
    Collection<Permission> permissions;

    private String filter;

    public PermissionEntity(Long folderId, PermissionArea area, Collection<Permission> permissions, String filter) {
        this.folderId = folderId;
        this.area = area;
        this.permissions = permissions;
        this.filter = filter;
    }
}
