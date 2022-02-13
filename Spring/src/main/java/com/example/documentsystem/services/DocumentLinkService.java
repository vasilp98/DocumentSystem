package com.example.documentsystem.services;

import com.example.documentsystem.entities.DocumentLinkEntity;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.DocumentLink;

import java.util.List;

public interface DocumentLinkService {
    Document getDocumentFromLink(String token, String password);

    DocumentLinkEntity create(DocumentLink link);
}
