package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.DocumentRepository;
import com.example.documentsystem.dao.ListRepository;
import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.entities.ListEntity;
import com.example.documentsystem.entities.PermissionEntity;
import com.example.documentsystem.entities.UserEntity;
import com.example.documentsystem.extensions.EntityExtensions;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.ListDto;
import com.example.documentsystem.models.filter.Filter;
import com.example.documentsystem.models.permission.Permission;
import com.example.documentsystem.services.*;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@ExtensionMethod(EntityExtensions.class)
public class ListServiceImpl implements ListService {
    private ListRepository listRepository;
    private UserService userService;
    private DocumentRepository documentRepository;
    private FilterService filterService;
    private PermissionService permissionService;

    public ListServiceImpl(
            ListRepository listRepository,
            UserService userService,
            DocumentRepository documentRepository,
            FilterService filterService,
            PermissionService permissionService) {

        this.listRepository = listRepository;
        this.userService = userService;
        this.documentRepository = documentRepository;
        this.filterService = filterService;
        this.permissionService = permissionService;
    }

    @Override
    public List<Document> getDocuments(Long listId) {
        ListDto listDto = findById(listId);
        List<DocumentEntity> documentEntities = documentRepository.findByFolderIdOrderById(listDto.getFolderId());

        List<DocumentEntity> documentsFromList = filterService.filterDocuments(documentEntities, Arrays.asList(listDto.getFilter()));
        return documentsFromList.stream().filter(d -> permissionService.hasDocumentPermission(d, Permission.READ) &&
                        d.getId() == d.getCurrentDocumentId()).map(de -> de.toDocument()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListDto> findAllInFolder(Long folderId) {
        return listRepository.findAllByFolderId(folderId).stream().map(l -> l.toDto()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListDto> findAllForCurrentUser() {
        UserEntity userEntity = userService.getCurrentUser();
        return listRepository.findAllByOwnerId(userEntity.getId()).stream().map(l -> l.toDto()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ListDto findById(Long id) {
        return listRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("List with ID=%s not found.", id))).toDto();
    }

    @Override
    public ListDto create(ListDto listDto) {
        UserEntity userEntity = userService.getCurrentUser();

        filterService.validateFilter(listDto.getFilter());

        return listRepository.save(new ListEntity(
                listDto.getFolderId(),
                userEntity.getId(),
                listDto.getName(),
                listDto.getFilter().serialize())).toDto();
    }

    @Override
    public ListDto deleteById(Long id) {
        ListDto old = findById(id);
        listRepository.deleteById(id);
        return old;
    }
}
