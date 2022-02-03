package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.DocumentRepository;
import com.example.documentsystem.dao.FileRepository;
import com.example.documentsystem.dao.content.FileContentId;
import com.example.documentsystem.dao.content.FileContentRepository;
import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.entities.FileEntity;
import com.example.documentsystem.extensions.EntityExtensions;
import com.example.documentsystem.models.ViewingDocumentBundle;
import com.example.documentsystem.services.DocumentService;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.InputStream;
import java.util.List;

@Service
@Transactional
@ExtensionMethod(EntityExtensions.class)
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final FileRepository fileRepository;
    private FileContentRepository fileContentRepository;

    @Autowired
    public DocumentServiceImpl(
            DocumentRepository documentRepository,
            FileRepository fileRepository,
            FileContentRepository fileContentRepository) {

        this.documentRepository = documentRepository;
        this.fileRepository = fileRepository;
        this.fileContentRepository = fileContentRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DocumentEntity> findAll() {
        return documentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentEntity findById(Long documentId) {
        return documentRepository.findById(documentId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Document with ID=%s not found.", documentId)));
    }

    @Override
    @Transactional(readOnly = true)
    public ViewingDocumentBundle getDocumentForViewing(Long id) {
        DocumentEntity documentEntity = findById(id);
        FileEntity firstFileEntity = fileRepository.findByDocumentIdAndNumber(id, 0L);

        InputStream firstFileStream = fileContentRepository.retrieve(new FileContentId(id, firstFileEntity.getId()));
        return new ViewingDocumentBundle(firstFileStream, documentEntity.toDocument());
    }

    @Override
    public DocumentEntity create(DocumentEntity document) {
        return documentRepository.save(document);
    }

    @Override
    public DocumentEntity update(DocumentEntity document) {
        return documentRepository.save(document);
    }

    @Override
    public DocumentEntity deleteById(Long documentId) {
        DocumentEntity old = findById(documentId);
        documentRepository.deleteById(documentId);
        return old;
    }
    
}
