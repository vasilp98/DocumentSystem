package com.example.documentsystem.services;


import com.example.documentsystem.entities.CommentEntity;

import java.util.List;

public interface CommentService {
    List<CommentEntity> findAll();

    CommentEntity findById(Long id);

    CommentEntity create(CommentEntity comment);

    CommentEntity update(CommentEntity comment);

    CommentEntity deleteById(Long id);
}
