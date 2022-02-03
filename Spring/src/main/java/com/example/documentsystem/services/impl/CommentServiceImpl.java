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
    private final CommentRepository commentRepository;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentEntity> findAll() {
        return commentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public CommentEntity findById(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Comment with ID=%s not found.", commentId)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CommentEntity> findAllByDocumentId(Long documentId) { return commentRepository.findAllByDocumentId((documentId)); }

    @Override
    public CommentEntity create(CommentEntity comment) {
        return commentRepository.save(comment);
    }

    @Override
    public CommentEntity update(CommentEntity comment) {
        return commentRepository.save(comment);
    }

    @Override
    public CommentEntity deleteById(Long commentId) {
        CommentEntity old = findById(commentId);
        commentRepository.deleteById(commentId);
        return old;
    }
}
