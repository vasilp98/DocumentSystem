package com.example.documentsystem.services;


import com.example.documentsystem.entities.CommentEntity;
import com.example.documentsystem.models.Comment;

import java.util.List;

public interface CommentService {
    List<CommentEntity> findAll();

    List<CommentEntity> findAllByDocumentId(Long documentId);

    CommentEntity findById(Long id);

    CommentEntity create(Long documentId, Comment comment);

    CommentEntity update(CommentEntity comment);

    CommentEntity deleteById(Long id);
}
