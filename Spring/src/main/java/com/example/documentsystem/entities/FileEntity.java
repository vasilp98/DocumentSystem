package com.example.documentsystem.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "files", uniqueConstraints = { @UniqueConstraint(columnNames = {"DOCUMENT_ID", "NUMBER"}) })
@Data
@NoArgsConstructor
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
    private Integer number;

    private String location;

    private String originalFileName;

    @NotNull
    private String extension;

    @NotNull
    private Long size;

    public FileEntity(Long documentId, Integer number, String originalFileName, String extension, Long size) {
        this.documentId = documentId;
        this.number = number;
        this.originalFileName = originalFileName;
        this.extension = extension;
        this.size = size;
    }

    public FileEntity(FileEntity fileEntity) {
        this.documentId = fileEntity.getDocumentId();
        this.number = fileEntity.getNumber();
        this.originalFileName = fileEntity.getOriginalFileName();
        this.extension = fileEntity.getExtension();
        this.size = fileEntity.getSize();
    }
}
