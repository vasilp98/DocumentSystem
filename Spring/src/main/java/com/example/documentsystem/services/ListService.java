package com.example.documentsystem.services;

import com.example.documentsystem.entities.FolderEntity;
import com.example.documentsystem.entities.ListEntity;
import com.example.documentsystem.models.filter.Filter;

import java.util.List;

public interface ListService {
    List<ListEntity> findAllInFolder(Long folderId);

    ListEntity findById(Long id);

    ListEntity create(Long folderId, List<Filter> filters);

    ListEntity update(Long id, List<Filter> filters);

    ListEntity deleteById(Long id);
}
