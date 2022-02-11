package com.example.documentsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.logging.Logger;

@Entity
@Table(name = "folders")
@Data
@NoArgsConstructor
public class FolderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private UserEntity owner;

    @Column(name = "owner_id")
    private Long ownerId;

    @NotNull
    private String name;

    @NotNull
    private LocalDateTime created = LocalDateTime.now();

    @NotNull
    @JsonIgnore
    private String storageLocation;

    private String customField1;
    private String customField2;
    private String customField3;
    private String customField4;
    private String customField5;

    public FolderEntity(Long ownerId, String name, LocalDateTime datetime, String storageLocation, String customField1, String customField2, String customField3, String customField4, String customField5) {
        setOwnerId(ownerId);
        setName(name);
        setCreated(datetime);
        setStorageLocation(storageLocation);
        setCustomField1(customField1);
        setCustomField2(customField2);
        setCustomField3(customField3);
        setCustomField4(customField4);
        setCustomField5(customField5);
    }
}
