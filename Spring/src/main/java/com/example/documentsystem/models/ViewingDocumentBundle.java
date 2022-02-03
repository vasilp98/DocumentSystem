package com.example.documentsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.InputStream;

@Data
@AllArgsConstructor
public class ViewingDocumentBundle {
    private InputStream firstFileStream;

    private Document document;
}
