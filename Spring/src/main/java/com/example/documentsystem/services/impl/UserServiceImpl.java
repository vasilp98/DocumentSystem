package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.UserRepository;
import com.example.documentsystem.entities.UserEntity;
import com.example.documentsystem.exceptions.InvalidEntityDataException;
import com.example.documentsystem.extensions.EntityExtensions;
import com.example.documentsystem.models.User;
import com.example.documentsystem.services.UserService;
import com.example.documentsystem.services.context.Context;
import lombok.experimental.ExtensionMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Transactional
@ExtensionMethod(EntityExtensions.class)
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
    public List<User> findAll() {
        return userRepository.findAll().stream().map(u -> u.toUser()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("User with ID=%s not found.", userId))).toUser();
    }

    @Override
    public User create(User user) {
        UserEntity userEntity = new UserEntity(
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()),
                user.getEmail()
        );

        return userRepository.save(userEntity).toUser();
    }

    @Override
    public User update(User user) {
        return user;
    }

    @Override
    public User deleteById(Long userId) {
        User old = findById(userId);
        userRepository.deleteById(userId);
        return old;
    }
}
