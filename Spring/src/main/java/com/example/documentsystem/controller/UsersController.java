package com.example.documentsystem.controller;

import com.example.documentsystem.entities.UserEntity;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.ListDto;
import com.example.documentsystem.models.User;
import com.example.documentsystem.services.ListService;
import com.example.documentsystem.services.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UsersController {
    private UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }

    @GetMapping("/{userId}")
    public User getUser(@PathVariable Long userId) {return userService.findById(userId);}

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @DeleteMapping("/{userId}")
    public User deleteUser(@PathVariable Long userId) {
        return userService.deleteById(userId);
    }
}
