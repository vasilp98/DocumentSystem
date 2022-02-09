package com.example.documentsystem.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "documents")
@Data
@NoArgsConstructor
public class DocumentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "FOLDER_ID", insertable = false, updatable = false)
    private FolderEntity folder;

    @Column(name = "FOLDER_ID")
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

    private Long currentDocumentId;

    private Integer versionNumber;

    public DocumentEntity(Long folderId, String name, String storeUser, String modifyUser, Long size, Integer filesCount) {
        this.folderId = folderId;
        this.name = name;
        this.storeUser = storeUser;
        this.modifyUser = modifyUser;
        this.size = size;
        this.filesCount = filesCount;
    }

    public DocumentEntity(DocumentEntity another) {
        this.folderId = another.getFolderId();
        this.name = another.getName();
        this.storeDate = another.getStoreDate();
        this.storeUser = another.getStoreUser();
        this.modifyDate = another.getModifyDate();
        this.modifyUser = another.getModifyUser();
        this.size = another.getSize();
        this.filesCount = another.getFilesCount();
        this.currentDocumentId = another.getCurrentDocumentId();
        this.versionNumber = another.getVersionNumber();
    }
}
