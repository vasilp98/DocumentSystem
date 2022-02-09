package com.example.documentsystem.dao;

import com.example.documentsystem.entities.ListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListRepository extends JpaRepository<ListEntity, Long> {
    List<ListEntity> findAllByFolderId(Long folderId);
}
