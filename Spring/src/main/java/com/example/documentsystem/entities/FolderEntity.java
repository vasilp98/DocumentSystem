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

    @ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "owner_id", insertable = false, updatable = false)
    private UserEntity owner;

    @Column(name = "owner_id")
    private Long ownerId;

    @NotNull
    private String Name;

    @NotNull
    private LocalDateTime created = LocalDateTime.now();

    @NotNull
    @JsonIgnore
    private String storageLocation;

    public FolderEntity(Long ownerId, String name, LocalDateTime datetime, String storageLocation) {
        setOwnerId(ownerId);
        setName(name);
        setCreated(datetime);
        setStorageLocation(storageLocation);
    }
}
