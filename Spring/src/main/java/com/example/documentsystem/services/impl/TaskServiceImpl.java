package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.TaskRepository;
import com.example.documentsystem.entities.TaskEntity;
import com.example.documentsystem.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class TaskServiceImpl implements TaskService {
    private final TaskRepository repo;

    @Autowired
    public TaskServiceImpl(TaskRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskEntity> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public TaskEntity findById(Long taskId) {
        return repo.findById(taskId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Task with ID=%s not found.", taskId)));
    }

    @Override
    public TaskEntity create(TaskEntity task) {
        return repo.save(task);
    }

    @Override
    public TaskEntity update(TaskEntity task) {
        return repo.save(task);
    }

    @Override
    public TaskEntity deleteById(Long taskId) {
        TaskEntity old = findById(taskId);
        repo.deleteById(taskId);
        return old;
    }
}
