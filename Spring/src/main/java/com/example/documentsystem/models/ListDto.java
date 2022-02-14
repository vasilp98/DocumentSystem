package com.example.documentsystem.models;

import com.example.documentsystem.models.filter.Filter;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
public class ListDto {
    private Long id;

    @NotNull
    private Long folderId;

    private Long ownerId;

    @NotNull
    private String name;

    private Filter filter;
}
