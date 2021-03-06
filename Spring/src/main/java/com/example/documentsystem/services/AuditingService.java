package com.example.documentsystem.services;

import com.example.documentsystem.models.auditing.AuditEventType;
import com.example.documentsystem.models.auditing.AuditEvent;

import java.util.List;

public interface AuditingService {
    void auditEvent(AuditEventType eventType, Long documentId);
    void deleteEvent(Long auditEventId);
    void deleteAllEvents(Long documentId);
    List<AuditEvent> getEventsForDocument(Long documentId);
}
