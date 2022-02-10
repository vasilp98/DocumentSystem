package com.example.documentsystem.dao;

import com.example.documentsystem.entities.ListEntity;
import com.example.documentsystem.entities.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    List<PermissionEntity> findAllByFolderId(Long folderId);
}
