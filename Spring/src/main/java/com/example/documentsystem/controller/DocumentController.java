package com.example.documentsystem.controller;

import com.example.documentsystem.entities.FileEntity;
import com.example.documentsystem.exceptions.FileNotFoundException;
import com.example.documentsystem.models.*;
import com.example.documentsystem.models.auditing.AuditEvent;
import com.example.documentsystem.services.AuditingService;
import com.example.documentsystem.services.DocumentService;
import com.example.documentsystem.services.DocumentVersionService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
@RequestMapping("/api/document")
public class DocumentController {
    private DocumentService documentService;
    private DocumentVersionService documentVersionService;

    @Autowired
    public DocumentController(DocumentService documentService, DocumentVersionService documentVersionService) {
        this.documentService = documentService;
        this.documentVersionService = documentVersionService;
    }

    @GetMapping("/{documentId}")
    public ViewingDocumentBundle findByDocumentId(@PathVariable Long documentId){
        return documentService.getDocumentForViewing(documentId);
    }

    @GetMapping("/{documentId}/files/{number}")
    public void getFile(HttpServletResponse response, @PathVariable Long documentId, @PathVariable Integer number) {
        FileStream fileStream = documentService.getFile(documentId, number);

        String contentType;
        try {
            contentType = Files.probeContentType(Path.of(fileStream.getName()));
        } catch (IOException e) {
            contentType = "txt/plain";
        }

        response.addHeader("Content-disposition", "attachment;filename=myfilename.txt");
        response.setContentType(contentType);

        try {
            IOUtils.copy(fileStream.getStream(), response.getOutputStream());
            response.flushBuffer();
        } catch (IOException exception) {
            throw new FileNotFoundException(exception.getMessage());
        }
    }

    @GetMapping("/{documentId}/files")
    public List<FileEntity> getFiles(@PathVariable Long documentId) {
        return documentService.getFiles(documentId);
    }

    @GetMapping("/{documentId}/versions")
    public List<Document> getVersionHistory(@PathVariable Long documentId) { return documentVersionService.findAllByDocumentId(documentId); }

    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public Document upload(@RequestPart StoredDocument document, @RequestPart MultipartFile file) {
        return documentService.create(document, file);
    }

    @PostMapping("/{documentId}/fields")
    public Document updateFields(@PathVariable Long documentId, @RequestBody DocumentUserFields fields) {
        return documentService.updateFields(documentId, fields);
    }

    @PostMapping("/{documentId}/files")
    public void addFile(@PathVariable Long documentId, @RequestParam("file") MultipartFile file) {
        documentService.addFile(documentId, file);
    }
}
