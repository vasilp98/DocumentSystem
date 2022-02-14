package com.example.documentsystem.models;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DocumentLinkPassword {
    @NotNull
    private String password;
}
