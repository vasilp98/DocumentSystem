package com.example.documentsystem.services;

import com.example.documentsystem.entities.FolderEntity;
import com.example.documentsystem.entities.ListEntity;
import com.example.documentsystem.models.ListDto;
import com.example.documentsystem.models.filter.Filter;

import java.util.List;

public interface ListService {
    List<ListDto> findAllInFolder(Long folderId);

    ListDto findById(Long id);

    ListDto create(Long folderId, List<Filter> filters);

    ListDto update(Long id, List<Filter> filters);

    ListDto deleteById(Long id);
}
