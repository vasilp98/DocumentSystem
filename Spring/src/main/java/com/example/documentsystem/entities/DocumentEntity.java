package com.example.documentsystem.entities;

import com.example.documentsystem.models.DocumentUserFields;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete( action = OnDeleteAction.CASCADE )
    private FolderEntity folder;

    @Column(name = "FOLDER_ID")
    private Long folderId;

    @NotNull
    private String name;

    @NotNull
    private Long storeDate = System.currentTimeMillis() / 1000L;;

    @NotNull
    private String storeUser;

    @NotNull
    private Long modifyDate = System.currentTimeMillis() / 1000L;

    @NotNull
    private String modifyUser;

    @NotNull
    private Long size;

    @NotNull
    private Integer filesCount;

    private Long currentDocumentId;

    private Integer versionNumber;

    //User fields

    private String documentType;

    private String company;

    private Long date;

    private String contact;

    private String status;

    private Double amount;

    private Integer number;

    public DocumentEntity(Long folderId, String name, String storeUser, String modifyUser, Long size, Integer filesCount) {
        this.folderId = folderId;
        this.name = name;
        this.storeUser = storeUser;
        this.modifyUser = modifyUser;
        this.size = size;
        this.filesCount = filesCount;
    }

    public DocumentEntity(Long folderId, String storeUser, String modifyUser, Long size, Integer filesCount, DocumentUserFields userFields) {
        this.folderId = folderId;
        this.storeUser = storeUser;
        this.modifyUser = modifyUser;
        this.size = size;
        this.filesCount = filesCount;
        this.name = userFields.getName();
        this.documentType = userFields.getDocumentType();
        this.company = userFields.getCompany();
        this.date = userFields.getDate();
        this.contact = userFields.getContact();
        this.status = userFields.getStatus();
        this.amount = userFields.getAmount();
        this.number = userFields.getNumber();
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
        this.documentType = another.getDocumentType();
        this.company = another.getCompany();
        this.date = another.getDate();
        this.contact = another.getContact();
        this.status = another.getStatus();
        this.amount = another.getAmount();
        this.number = another.getNumber();
    }
}
