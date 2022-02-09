package com.example.documentsystem.controller;

import com.example.documentsystem.exceptions.FileNotFoundException;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.services.DocumentVersionService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api/version")
public class DocumentVersionController {
    private DocumentVersionService documentVersionService;

    @Autowired
    public DocumentVersionController(DocumentVersionService documentVersionService) {
        this.documentVersionService = documentVersionService;
    }

    @PostMapping("/{documentId}")
    public Document createVersion(@PathVariable Long documentId) {
        return documentVersionService.create(documentId);
    }

    @GetMapping("/{id}")
    public Document getVersion(@PathVariable Long id) { return documentVersionService.findById(id); }

    @GetMapping("/{id}/files/{number}")
    public void getFile(HttpServletResponse response, @PathVariable Long id, @PathVariable Integer number) {
        InputStream fileStream = documentVersionService.getFile(id, number);

        response.addHeader("Content-disposition", "attachment;filename=myfilename.txt");
        response.setContentType("txt/plain");

        try {
            IOUtils.copy(fileStream, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException exception) {
            throw new FileNotFoundException(exception.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public Document deleteVersion(@PathVariable Long id) { return documentVersionService.deleteById(id); }
}

