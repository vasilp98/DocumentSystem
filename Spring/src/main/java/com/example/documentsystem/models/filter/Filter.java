package com.example.documentsystem.models.filter;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Filter {
    private String field;

    private Operation operation;

    private String value;
}
