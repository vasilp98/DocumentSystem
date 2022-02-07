package com.example.documentsystem.models;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class StoredDocument {
    @NotNull
    private Long folderId;

    private DocumentUserFields userFields;
}
