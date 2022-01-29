package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.DocumentLinkRepository;
import com.example.documentsystem.entities.DocumentLinkEntity;
import com.example.documentsystem.services.DocumentLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;


@Service
@Transactional
public class DocumentLinkServiceImpl implements DocumentLinkService {
    private final DocumentLinkRepository repo;

    @Autowired
    public DocumentLinkServiceImpl(DocumentLinkRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DocumentLinkEntity> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public DocumentLinkEntity findById(Long linkId) {
        return repo.findById(linkId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Link with ID=%s not found.", linkId)));
    }

    @Override
    public DocumentLinkEntity create(DocumentLinkEntity link) {
        return repo.save(link);
    }

    @Override
    public DocumentLinkEntity update(DocumentLinkEntity link) {
        return repo.save(link);
    }

    @Override
    public DocumentLinkEntity deleteById(Long linkId) {
        DocumentLinkEntity old = findById(linkId);
        repo.deleteById(linkId);
        return old;
    }
}
