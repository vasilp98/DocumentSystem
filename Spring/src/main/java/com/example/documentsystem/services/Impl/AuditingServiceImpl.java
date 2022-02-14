package com.example.documentsystem.services.impl;

import com.example.documentsystem.models.auditing.AuditEventType;
import com.example.documentsystem.dao.AuditRepository;
import com.example.documentsystem.entities.AuditEntity;
import com.example.documentsystem.models.auditing.AuditEvent;
import com.example.documentsystem.services.AuditingService;
import com.example.documentsystem.services.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class AuditingServiceImpl implements AuditingService {
    private final AuditRepository auditRepository;

    @Autowired
    public AuditingServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public void auditEvent(AuditEventType eventType, Long documentId) {
        String username = Context.getCurrentUserName();

        AuditEntity auditEntity = new AuditEntity(documentId, eventType.toString(), LocalDateTime.now(), username);
        auditRepository.save(auditEntity);
    }

    @Override
    public void deleteEvent(Long auditEventId) {
        auditRepository.deleteById(auditEventId);
    }

    @Override
    public void deleteAllEvents(Long documentId) {
        auditRepository.removeByDocumentId(documentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AuditEvent> getEventsForDocument(Long documentId) {
        List<AuditEntity> auditEntities = auditRepository.findAllByDocumentId(documentId);

        return auditEntities.stream().map(e ->
                new AuditEvent(
                        e.getId(),
                        e.getDocumentId(),
                        AuditEventType.valueOf(e.getEventType()),
                        e.getEventDate(),
                        e.getUserInitiated())).collect(Collectors.toList());
    }
}
