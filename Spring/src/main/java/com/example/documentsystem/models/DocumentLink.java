package com.example.documentsystem.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentLink {
    private Long documentId;

    private String password;

    private LocalDateTime validUntil;
}