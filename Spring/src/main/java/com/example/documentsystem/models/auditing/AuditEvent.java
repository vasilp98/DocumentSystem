package com.example.documentsystem.models.auditing;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class AuditEvent {
    private Long id;

    private Long documentId;

    private AuditEventType eventType;

    private LocalDateTime eventDate;

    private String userInitiated;

    private List<AuditedField> details;
}
