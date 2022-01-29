package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.DocumentRepository;
import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.services.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository repo;
    
    @Autowired
    public DocumentServiceImpl(DocumentRepository repo) {
        this.repo = repo;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<DocumentEntity> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentEntity findById(Long documentId) {
        return repo.findById(documentId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Document with ID=%s not found.", documentId)));
    }

    @Override
    public DocumentEntity create(DocumentEntity document) {
        return repo.save(document);
    }

    @Override
    public DocumentEntity update(DocumentEntity document) {
        return repo.save(document);
    }

    @Override
    public DocumentEntity deleteById(Long documentId) {
        DocumentEntity old = findById(documentId);
        repo.deleteById(documentId);
        return old;
    }
    
}
