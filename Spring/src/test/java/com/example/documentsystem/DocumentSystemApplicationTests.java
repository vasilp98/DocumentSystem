package com.example.documentsystem;

import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.models.auditing.AuditedField;
import com.example.documentsystem.dao.AuditRepository;
import com.example.documentsystem.entities.AuditEntity;
import com.example.documentsystem.models.filter.Filter;
import com.example.documentsystem.models.filter.Operation;
import com.example.documentsystem.models.permission.Permission;
import com.example.documentsystem.models.permission.PermissionArea;
import com.example.documentsystem.models.permission.PermissionDto;
import com.example.documentsystem.services.AuditingService;
import com.example.documentsystem.services.FilterService;
import com.example.documentsystem.services.FolderService;
import com.example.documentsystem.services.impl.AuditingServiceImpl;
import com.example.documentsystem.services.impl.FilterServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class DocumentSystemApplicationTests {

    @Mock
    private AuditRepository auditRepository;

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

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void uploadDocument() throws Exception {
        MockMultipartFile documentJSON = new MockMultipartFile("document", null,
                "application/json", "{\"userFields\": {\"name\" : \"Document\"}, \"folderId\" : \"2\"}".getBytes());

        FileInputStream fileInputStream = new FileInputStream("/Users/vasilp/Desktop/Vasko/Staj/otchet.pdf");
        MockMultipartFile file = new MockMultipartFile("file", "otchet.pdf", "application/pdf", fileInputStream);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/api/document")
                        .file(file)
                        .file(documentJSON))
                .andExpect(status().isOk());
    }

    @Test
    public void addFile() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("/Users/vasilp/Desktop/Vasko/Staj/otchet.pdf");
        MockMultipartFile file = new MockMultipartFile("file", "otchet.pdf", "application/pdf", fileInputStream);

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(multipart("/api/document/16/files")
                        .file(file))
                .andExpect(status().isOk());
    }

    @Test
    public void addPermission() throws Exception {
        List<Long> users = new ArrayList<>();
        users.add(1L);
        users.add(2L);

        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.READ);

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("storeUser", Operation.EQUAL, "admin"));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/folder/1/permissions")
                        .content(asJsonString(
                                new PermissionDto(1L, 1L, users, PermissionArea.FOLDER, permissions, filters)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updatePermission() throws Exception {
        List<Long> users = new ArrayList<>();
        users.add(1L);
        users.add(2L);

        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.READ);

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("storeUser", Operation.EQUAL, "admin"));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.post("/api/folder/1/permissions")
                        .content(asJsonString(
                                new PermissionDto(1L, 1L, null, PermissionArea.FOLDER, permissions, filters)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/folder/1/permissions/1")
                        .content(asJsonString(
                                new PermissionDto(1L, 1L, users, PermissionArea.FOLDER, null, null)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getFolder() throws Exception {
        List<Long> users = new ArrayList<>();
        users.add(1L);
        users.add(2L);

        List<Permission> permissions = new ArrayList<>();
        permissions.add(Permission.DELETE);

        List<Filter> filters = new ArrayList<>();
        filters.add(new Filter("storeUser", Operation.EQUAL, "admin"));

        MockMvc mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        mockMvc.perform(MockMvcRequestBuilders.get("/api/folder/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void filterDocuments() {
        FilterService filterService = new FilterServiceImpl();

        DocumentEntity documentEntity = new DocumentEntity(1L, "Document", "admin", "admin", 4L, 2);
        Filter filter = new Filter("storeDate", Operation.EQUAL, "2022-02-09");

        List<Filter> filters = new ArrayList<>();
        filters.add(filter);

        Assertions.assertTrue(filterService.checkDocument(documentEntity, filters));
    }
}
