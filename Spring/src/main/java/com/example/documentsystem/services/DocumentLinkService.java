package com.example.documentsystem.services;

import com.example.documentsystem.entities.DocumentLinkEntity;

import java.util.List;

public interface DocumentLinkService {
    List<DocumentLinkEntity> findAll();

    DocumentLinkEntity findById(Long id);

    DocumentLinkEntity create(DocumentLinkEntity link);

    DocumentLinkEntity update(DocumentLinkEntity link);

    DocumentLinkEntity deleteById(Long id);
}
