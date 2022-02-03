package com.example.documentsystem;

import com.example.documentsystem.models.auditing.AuditedField;
import com.example.documentsystem.dao.AuditRepository;
import com.example.documentsystem.entities.AuditEntity;
import com.example.documentsystem.services.AuditingService;
import com.example.documentsystem.services.impl.AuditingServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@SpringBootTest
class DocumentSystemApplicationTests {

    @Mock
    private AuditRepository auditRepository;

//    @Before
//    public void setUp() {
//        //MockitoAnnotations.initMocks(this);
//        //when(auditRepository.findById(1L)).thenReturn(Optional.of(book));
//    }

    @Test
    void contextLoads() {
        AuditingService auditingService = new AuditingServiceImpl(auditRepository);
        List<AuditedField> auditedFields = new ArrayList<>();
        auditedFields.add(new AuditedField("name", "old", "new"));

        auditingService.auditStore(auditedFields, 1L);
        AuditEntity auditEntity = new AuditEntity(1L, "STORE", LocalDateTime.now(), "", "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><auditedFields><item><name>name</name><newValue>new</newValue><oldValue>old</oldValue></item></auditedFields>");
        when(auditRepository.findAllByDocumentId(1L)).thenReturn(new ArrayList<>() { {add(auditEntity);} });
        var events = auditingService.getEventsForDocument(1L);
    }

}
