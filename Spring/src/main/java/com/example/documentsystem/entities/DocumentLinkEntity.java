package com.example.documentsystem.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "links")
@Data
public class DocumentLinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String token;

    @NotNull
    @ManyToOne(targetEntity = DocumentEntity.class)
    @JoinColumn(name = "DOCUMENT_ID", nullable = false)
    private Long documentId;

    private String password;

    private LocalDateTime validUntil;
}
