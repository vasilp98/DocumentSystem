package com.example.documentsystem.models;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
public class StoredDocument {
    @NotNull
    private String name;

    @NotNull
    private Long folderId;

    //TODO Fields
}
