package com.example.documentsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class FileStream {
    private InputStream stream;

    private String name;
}
