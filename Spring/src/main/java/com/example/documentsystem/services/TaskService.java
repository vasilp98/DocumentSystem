package com.example.documentsystem.services;

import com.example.documentsystem.entities.TaskEntity;

import java.util.List;

public interface TaskService {
    List<TaskEntity> findAll();

    TaskEntity findById(Long id);

    TaskEntity create(TaskEntity org);

    TaskEntity update(TaskEntity org);

    TaskEntity deleteById(Long id);
}
