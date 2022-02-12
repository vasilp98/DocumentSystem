package com.example.documentsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FolderDto {
    private String name;

    private String storageLocation;
}
