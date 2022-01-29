package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.CommentRepository;
import com.example.documentsystem.entities.CommentEntity;
import com.example.documentsystem.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository repo;

    @Autowired
    public CommentServiceImpl(CommentRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentEntity> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CommentEntity findById(Long commentId) {
        return repo.findById(commentId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Comment with ID=%s not found.", commentId)));
    }

    @Override
    public CommentEntity create(CommentEntity comment) {
        return repo.save(comment);
    }

    @Override
    public CommentEntity update(CommentEntity comment) {
        return repo.save(comment);
    }

    @Override
    public CommentEntity deleteById(Long commentId) {
        CommentEntity old = findById(commentId);
        repo.deleteById(commentId);
        return old;
    }
}
