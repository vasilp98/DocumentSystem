package com.example.documentsystem.services;

import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.models.filter.Filter;

import java.util.List;

public interface FilterService {
    List<DocumentEntity> filterDocuments(List<DocumentEntity> documentEntities, List<Filter> filters);

    boolean checkDocument(DocumentEntity documentEntity, List<Filter> filters);

    void validateFilter(Filter filter);
}
