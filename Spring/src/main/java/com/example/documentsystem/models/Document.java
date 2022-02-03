package com.example.documentsystem.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class Document {
    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private LocalDateTime storeDate = LocalDateTime.now();

    @NotNull
    private String storeUser;

    @NotNull
    private LocalDateTime modifyDate = LocalDateTime.now();

    @NotNull
    private String modifyUser;

    @NotNull
    private Long size;

    @NotNull
    private Integer filesCount;
}
