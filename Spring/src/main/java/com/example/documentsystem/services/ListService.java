package com.example.documentsystem.services;

import com.example.documentsystem.entities.FolderEntity;
import com.example.documentsystem.entities.ListEntity;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.ListDto;
import com.example.documentsystem.models.filter.Filter;

import java.util.List;

public interface ListService {
    List<Document> getDocuments(Long listId);

    List<ListDto> findAllInFolder(Long folderId);

    List<ListDto> findAllForCurrentUser();

    ListDto findById(Long id);

    ListDto create(ListDto listDto);

    ListDto deleteById(Long id);
}
