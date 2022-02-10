package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.UserRepository;
import com.example.documentsystem.entities.UserEntity;
import com.example.documentsystem.services.UserService;
import com.example.documentsystem.services.context.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity getCurrentUser() {
        String username = Context.getCurrentUserName();
        return userRepository.findByUsername(username).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("User with username=%s not found or you don't have permissions to access it.", username)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public UserEntity findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("User with ID=%s not found.", userId)));
    }

    @Override
    public UserEntity create(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity update(UserEntity userEntity) {
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        return userRepository.save(userEntity);
    }

    @Override
    public UserEntity deleteById(Long userId) {
        UserEntity old = findById(userId);
        userRepository.deleteById(userId);
        return old;
    }
}
