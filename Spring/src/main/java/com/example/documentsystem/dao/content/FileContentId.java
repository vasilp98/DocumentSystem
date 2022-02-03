package com.example.documentsystem.dao.content;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FileContentId {
    private Long documentId;

    private Long fileId;
}
