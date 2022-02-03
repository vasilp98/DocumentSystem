package com.example.documentsystem.services;

import com.example.documentsystem.entities.TaskEntity;
import com.example.documentsystem.entities.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> findAll();

    UserEntity findById(Long id);

    UserEntity create(UserEntity userEntity);

    UserEntity update(UserEntity userEntity);

    UserEntity deleteById(Long id);
}
