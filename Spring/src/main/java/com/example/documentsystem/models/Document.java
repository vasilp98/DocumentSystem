package com.example.documentsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Document {
    private Long id;

    private LocalDate storeDate;

    private String storeUser;

    private LocalDate modifyDate;

    private String modifyUser;

    private Long size;

    private Integer filesCount;

    private Integer versionNumber;

    private Long currentDocumentId;

    private DocumentUserFields userFields;
}
