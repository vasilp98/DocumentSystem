package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.DocumentVersionRepository;
import com.example.documentsystem.entities.DocumentVersionEntity;
import com.example.documentsystem.services.DocumentVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@Service
@Transactional
public class DocumentVersionServiceImpl implements DocumentVersionService {
    private final DocumentVersionRepository repo;

    @Autowired
    public DocumentVersionServiceImpl(DocumentVersionRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentVersionEntity> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentVersionEntity findById(Long versionId) {
        return repo.findById(versionId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Version with ID=%s not found.", versionId)));
    }

    @Override
    public DocumentVersionEntity create(DocumentVersionEntity version) {
        return repo.save(version);
    }

    @Override
    public DocumentVersionEntity update(DocumentVersionEntity version) {
        return repo.save(version);
    }

    @Override
    public DocumentVersionEntity deleteById(Long versionId) {
        DocumentVersionEntity old = findById(versionId);
        repo.deleteById(versionId);
        return old;
    }
}
