package com.example.documentsystem.models.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Filter {
    private String field;

    private Operation operation;

    private String value;
}
