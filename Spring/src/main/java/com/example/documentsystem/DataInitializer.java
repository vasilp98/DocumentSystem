package com.example.documentsystem;

import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.entities.UserEntity;
import com.example.documentsystem.entities.FolderEntity;
import com.example.documentsystem.services.DocumentService;
import com.example.documentsystem.services.UserService;
import com.example.documentsystem.services.FolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class DataInitializer implements ApplicationRunner {
    @Autowired
    private UserService userService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private DocumentService documentService;

    private final List<UserEntity> DEFAULT_USERS = List.of(
            new UserEntity("admin", "Admin123&", "admin@admin.com"),
            new UserEntity("user", "user", "user@user.com")
    );

    private final List<FolderEntity> DEFAULT_FOLDERS = List.of(
        // fix hardcoded values!
        new FolderEntity(5L, "Papka", LocalDateTime.now(), "polojenie"),
        new FolderEntity(6L, "Papka2", LocalDateTime.now(), "polojenie2")
    );

    private final List<DocumentEntity> DEFAULT_DOCUMENTS = List.of(
            // fix hardcoded values!
            new DocumentEntity(2L, "Document", "admin", "admin", 4L, 2),
            new DocumentEntity(2L, "Document2", "user", "user", 4L, 2)
    );

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(userService.findAll().size() == 0) {
            log.info("Successfully created users: {}",
                    DEFAULT_USERS.stream().map(userService::create).collect(Collectors.toList()));
        }

        if(folderService.findAll().size() == 0){
            log.info("Successfully created folders: {}",
                    DEFAULT_FOLDERS.stream().map(folderService::create).collect(Collectors.toList()));
        }

        if(documentService.findAll().size() == 0){
            log.info("Successfully created documents: {}",
                    DEFAULT_DOCUMENTS.stream().map(documentService::create).collect(Collectors.toList()));
        }
    }
}
