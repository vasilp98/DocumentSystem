package com.example.documentsystem.services;

import com.example.documentsystem.entities.DocumentLinkEntity;
import com.example.documentsystem.models.DocumentLink;

import java.util.List;

public interface DocumentLinkService {
    Long getDocumentIdFromLink(String token, String password);

    DocumentLinkEntity create(DocumentLink link);
}
