package com.example.documentsystem;

import com.example.documentsystem.dao.UserRepository;
import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.entities.UserEntity;
import com.example.documentsystem.entities.FolderEntity;
import com.example.documentsystem.models.FolderDto;
import com.example.documentsystem.models.User;
import com.example.documentsystem.models.UserRole;
import com.example.documentsystem.services.DocumentService;
import com.example.documentsystem.services.UserService;
import com.example.documentsystem.services.FolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j

public class DataInitializer implements ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if(userRepository.count() == 0) {
            UserEntity userEntity = new UserEntity("admin",
                    passwordEncoder.encode("Admin123&"),
                    "admin@admin.com");

            userEntity.setRole(UserRole.ADMIN);

            userRepository.save(userEntity);
        }
    }
}
