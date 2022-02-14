package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.CommentRepository;
import com.example.documentsystem.entities.CommentEntity;
import com.example.documentsystem.models.Comment;
import com.example.documentsystem.services.CommentService;
import com.example.documentsystem.services.UserService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private UserService userService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
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
    public List<CommentEntity> findAllByDocumentId(Long documentId) { return commentRepository.findAllByDocumentIdOrderByCreatedAsc((documentId)); }

    @Override
    public CommentEntity create(Long documentId, Comment comment) {
        return commentRepository.save(new CommentEntity(documentId, userService.getCurrentUser().getUsername(), comment.getContent()));
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
