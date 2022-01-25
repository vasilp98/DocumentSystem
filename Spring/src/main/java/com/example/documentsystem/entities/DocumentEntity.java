package com.example.documentsystem.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "documents")
@Data
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = FolderEntity.class)
    @JoinColumn(name = "FOLDER_ID", nullable = false)
    private Long folderId;

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
