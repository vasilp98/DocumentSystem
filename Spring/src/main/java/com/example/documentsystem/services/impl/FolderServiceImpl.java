package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.FolderRepository;
import com.example.documentsystem.entities.FolderEntity;
import com.example.documentsystem.services.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class FolderServiceImpl implements FolderService {
    private final FolderRepository repo;

    @Autowired
    public FolderServiceImpl(FolderRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FolderEntity> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public FolderEntity findById(Long folderId) {
        return repo.findById(folderId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Folder with ID=%s not found.", folderId)));
    }

    @Override
    public FolderEntity create(FolderEntity folder) {
        return repo.save(folder);
    }

    @Override
    public FolderEntity update(FolderEntity folder) {
        return repo.save(folder);
    }

    @Override
    public FolderEntity deleteById(Long folderId) {
        FolderEntity old = findById(folderId);
        repo.deleteById(folderId);
        return old;
    }
}
