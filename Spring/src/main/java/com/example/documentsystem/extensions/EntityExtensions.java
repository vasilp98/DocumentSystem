package com.example.documentsystem.extensions;

import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.models.Document;

public class EntityExtensions {
    public static Document toDocument(DocumentEntity documentEntity) {
        return new Document(
                documentEntity.getId(),
                documentEntity.getName(),
                documentEntity.getStoreDate(),
                documentEntity.getStoreUser(),
                documentEntity.getModifyDate(),
                documentEntity.getModifyUser(),
                documentEntity.getSize(),
                documentEntity.getFilesCount()
        );
    }
}
