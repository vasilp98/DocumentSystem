package com.example.documentsystem.models;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class DocumentLink {
    @NotNull
    private Long documentId;

    @NotNull
    private String password;

    private LocalDate validUntil;
}
