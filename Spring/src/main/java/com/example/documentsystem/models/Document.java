package com.example.documentsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Document {
    @NotNull
    private Long id;

    @NotNull
    private Long storeDate;

    @NotNull
    private String storeUser;

    @NotNull
    private Long modifyDate;

    @NotNull
    private String modifyUser;

    @NotNull
    private Long size;

    @NotNull
    private Integer filesCount;

    private Integer versionNumber;

    private Long currentDocumentId;

    private DocumentUserFields userFields;
}
