package com.example.documentsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Comment {
    @NotNull
    private Long documentId;

    @NotNull
    private String content;
}
