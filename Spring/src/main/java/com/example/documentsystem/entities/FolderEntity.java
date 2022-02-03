package com.example.documentsystem.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "folders")
@Data
public class FolderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(targetEntity = UserEntity.class)
    @JoinColumn(name = "OWNER_ID", nullable = false)
    private Long ownerId;

    @NotNull
    private String Name;

    @NotNull
    private LocalDateTime created = LocalDateTime.now();

    @NotNull
    @JsonIgnore
    private String storageLocation;
}
