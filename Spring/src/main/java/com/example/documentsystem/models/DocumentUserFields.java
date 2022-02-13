package com.example.documentsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class DocumentUserFields {
    private String name;

    private String documentType;

    private String company;

    private Long date;

    private String contact;

    private String status;

    private Double amount;

    private Integer number;
}
