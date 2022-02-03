package com.example.documentsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Comment {
    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    private String createdUser;

    @NotNull
    private String content;

    @NotNull
    private LocalDateTime created;

    @NotNull
    private LocalDateTime modified;
}
