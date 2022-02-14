package com.example.documentsystem.models;

import com.example.documentsystem.models.filter.Filter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ListDto {
    private Long id;

    private Long folderId;

    private Long ownerId;

    private String name;

    private Filter filter;
}
