package com.example.documentsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class FolderDto {
    @NotNull
    private String name;

    @NotNull
    private String storageLocation;
}
