package com.example.documentsystem;

import com.example.documentsystem.entities.UserEntity;
import com.example.documentsystem.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserService userService;

    private final List<UserEntity> DEFAULT_USERS = List.of(
            new UserEntity("admin", "Admin123&", "admin@admin.com"),
            new UserEntity("user", "user", "user@user.com")
    );

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(userService.findAll().size() == 0) {
            log.info("Successfully created users: {}",
                    DEFAULT_USERS.stream().map(userService::create).collect(Collectors.toList()));
        }
    }
}
