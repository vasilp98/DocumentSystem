package com.example.documentsystem.entities;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditing")
@Data
public class AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long documentId;

    @NotNull
    private String eventType;

    @NotNull
    private LocalDateTime eventDate;

    private String userInitiated;

    private String details;
}
