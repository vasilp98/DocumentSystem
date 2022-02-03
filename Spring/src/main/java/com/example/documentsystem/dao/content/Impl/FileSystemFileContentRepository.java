package com.example.documentsystem.dao.content.Impl;

import com.example.documentsystem.dao.DocumentRepository;
import com.example.documentsystem.dao.FileRepository;
import com.example.documentsystem.dao.FolderRepository;
import com.example.documentsystem.dao.content.FileContentId;
import com.example.documentsystem.dao.content.FileContentRepository;
import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.entities.FileEntity;
import com.example.documentsystem.entities.FolderEntity;
import com.example.documentsystem.exceptions.FileNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
public class FileSystemFileContentRepository implements FileContentRepository {
    private FolderRepository folderRepository;
    private DocumentRepository documentRepository;
    private FileRepository fileRepository;

    @Autowired
    public FileSystemFileContentRepository(
            FolderRepository folderRepository,
            DocumentRepository documentRepository,
            FileRepository fileRepository) {

        this.folderRepository = folderRepository;
        this.documentRepository = documentRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public InputStream retrieve(FileContentId id) {
        Path path = getLocation(id);

        try {
            return Files.newInputStream(path);
        } catch (IOException exception) {
            throw new FileNotFoundException(String.format("File with name '%s' can't be found!", path));
        }
    }

    @Override
    public void add(FileContentId id, InputStream stream) throws IOException {
        Path path = generateLocation(id);
        Files.copy(stream, path);
    }

    @Override
    public void update(FileContentId id, InputStream stream) throws IOException {
        delete(id);
        add(id, stream);
    }

    @Override
    public void delete(FileContentId id) throws IOException {
        Path path = getLocation(id);
        Files.delete(path);
    }

    private Path getLocation(FileContentId id) {
        DocumentEntity documentEntity = documentRepository.getById(id.getDocumentId());
        FolderEntity folderEntity = folderRepository.getById(documentEntity.getFolderId());
        FileEntity fileEntity = fileRepository.getById(id.getFileId());

        return Path.of(folderEntity.getStorageLocation(), fileEntity.getLocation());
    }

    private Path generateLocation(FileContentId id) {
        DocumentEntity documentEntity = documentRepository.getById(id.getDocumentId());
        FolderEntity folderEntity = folderRepository.getById(documentEntity.getFolderId());
        FileEntity fileEntity = fileRepository.getById(id.getFileId());

        String relativePath = Path.of(folderEntity.getName(),
                documentEntity.getId().toString(),
                UUID.randomUUID() + fileEntity.getExtension()).toString();

        fileEntity.setLocation(relativePath);
        fileRepository.save(fileEntity);

        return Path.of(folderEntity.getStorageLocation(), relativePath);
    }
}
