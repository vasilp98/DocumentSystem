package com.example.documentsystem.models;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DocumentUserFields {
    private String name;

    private String documentType;

    private String company;

    private LocalDateTime date;

    private String contact;

    private String status;

    private Double amount;

    private Integer number;
}
