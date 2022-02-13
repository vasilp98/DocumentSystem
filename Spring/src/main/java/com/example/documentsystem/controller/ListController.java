package com.example.documentsystem.controller;

import com.example.documentsystem.entities.ListEntity;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.ListDto;
import com.example.documentsystem.models.permission.PermissionDto;
import com.example.documentsystem.services.ListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/list")
public class ListController {
    private ListService listService;

    public ListController(ListService listService) {
        this.listService = listService;
    }

    @GetMapping("/{folderId}")
    public List<ListDto> getAllInFolder(@PathVariable Long folderId) {
        return listService.findAllInFolder(folderId);
    }

    @GetMapping
    public List<ListDto> getAllForUser() {
        return listService.findAllForCurrentUser();
    }

    @GetMapping("/{listId}")
    public ListDto getList(@PathVariable Long listId) {
        return listService.findById(listId);
    }

    @GetMapping("/{listId}/documents")
    public List<Document> getDocumentInList(@PathVariable Long listId) {
        return listService.getDocuments(listId);
    }

    @PostMapping
    public ListDto createList(@RequestBody ListDto listDto) {
        return listService.create(listDto);
    }

    @DeleteMapping("/{listId}")
    public ListDto deleteList(@PathVariable Long listId) {
        return listService.deleteById(listId);
    }
}
