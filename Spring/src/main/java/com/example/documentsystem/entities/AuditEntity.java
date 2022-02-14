package com.example.documentsystem.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "auditing")
@Data
@NoArgsConstructor
public class AuditEntity {
    public AuditEntity(Long documentId, String eventType, LocalDateTime eventDate, String userInitiated) {
        this.documentId = documentId;
        this.eventType = eventType;
        this.eventDate = eventDate;
        this.userInitiated = userInitiated;
    }

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
}
