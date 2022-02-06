package com.example.documentsystem.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "files", uniqueConstraints = { @UniqueConstraint(columnNames = {"DOCUMENT_ID", "NUMBER"}) })
@Data
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "DOCUMENT_ID", insertable = false, updatable = false)
    private DocumentEntity document;

    @Column(name = "DOCUMENT_ID")
    private Long documentId;

    @NotNull
    private Long number;

    @NotNull
    private String location;

    @NotNull
    private String extension;

    @NotNull
    private Long size;
}
