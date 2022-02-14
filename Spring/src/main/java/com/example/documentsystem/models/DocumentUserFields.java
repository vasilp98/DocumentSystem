package com.example.documentsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class DocumentUserFields {
    private String name;

    private String documentType;

    private String company;

    private LocalDate date;

    private String contact;

    private String status;

    private Integer amount;

    private Integer number;
}
