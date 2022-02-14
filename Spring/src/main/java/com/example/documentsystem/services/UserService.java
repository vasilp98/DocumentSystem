package com.example.documentsystem.services;

import com.example.documentsystem.entities.TaskEntity;
import com.example.documentsystem.entities.UserEntity;
import com.example.documentsystem.models.User;

import java.util.List;

public interface UserService {
    UserEntity getCurrentUser();

    List<User> findAll();

    User findById(Long id);

    User create(User userEntity);

    User update(User userEntity);

    User deleteById(Long id);
}
