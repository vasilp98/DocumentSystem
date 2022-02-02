package com.example.documentsystem.services.impl;

import com.example.documentsystem.models.auditing.AuditEventType;
import com.example.documentsystem.models.auditing.AuditedField;
import com.example.documentsystem.models.auditing.AuditedFieldList;
import com.example.documentsystem.dao.AuditRepository;
import com.example.documentsystem.entities.AuditEntity;
import com.example.documentsystem.models.auditing.AuditEvent;
import com.example.documentsystem.services.AuditingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditingServiceImpl implements AuditingService {
    private final AuditRepository auditRepository;

    @Autowired
    public AuditingServiceImpl(AuditRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @Override
    public void auditEvent(AuditEventType eventType, Long documentId) {
        String username = getCurrentUserName();

        AuditEntity auditEntity = new AuditEntity(documentId, eventType.toString(), LocalDateTime.now(), username, null);
        auditRepository.save(auditEntity);
    }

    @Override
    public void auditUpdateFields(List<AuditedField> auditedFields, Long documentId) {
        String username = getCurrentUserName();
        String details = getDetailsAsString(auditedFields);

        AuditEntity auditEntity = new AuditEntity(documentId, AuditEventType.UPDATE_FIELDS.name(), LocalDateTime.now(), username, details);
        auditRepository.save(auditEntity);
    }

    @Override
    public void auditStore(List<AuditedField> auditedFields, Long documentId) {
        String username = getCurrentUserName();
        String details = getDetailsAsString(auditedFields);

        AuditEntity auditEntity = new AuditEntity(documentId, AuditEventType.STORE.name(), LocalDateTime.now(), username, details);
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
    public List<AuditEvent> getEventsForDocument(Long documentId) {
        List<AuditEntity> auditEntities = auditRepository.findAllByDocumentId(documentId);

        return auditEntities.stream().map(e ->
                new AuditEvent(
                        e.getId(),
                        e.getDocumentId(),
                        AuditEventType.valueOf(e.getEventType()),
                        e.getEventDate(),
                        e.getUserInitiated(),
                        getAuditedFields(e.getDetails()))).collect(Collectors.toList());
    }

    private String getCurrentUserName() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails)principal).getUsername();
        } else {
            return principal.toString();
        }
    }

    private String getDetailsAsString(List<AuditedField> auditedFields) {
        try {
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(AuditedFieldList.class);

            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(new AuditedFieldList(auditedFields), writer);

            return writer.toString();
        } catch (JAXBException exception) {
            throw new ValidationException(exception.getMessage());
        }
    }

    private List<AuditedField> getAuditedFields(String details) {
        try {
            StringReader reader = new StringReader(details);
            JAXBContext context = JAXBContext.newInstance(AuditedFieldList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return ((AuditedFieldList) unmarshaller.unmarshal(reader)).getAuditedFields();
        } catch (JAXBException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}
