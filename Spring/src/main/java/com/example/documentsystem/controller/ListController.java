package com.example.documentsystem.controller;

import com.example.documentsystem.entities.ListEntity;
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

    @GetMapping("/{listId}")
    public ListDto getList(@PathVariable Long listId) {
        return listService.findById(listId);
    }

    @PostMapping
    public ListDto createList(@RequestBody ListDto listDto) {
        return listService.create(listDto.getFolderId(), listDto.getFilters());
    }

    @PostMapping("/{listId}")
    public ListDto updateList(@PathVariable Long listId, @RequestBody ListDto listDto) {
        return listService.update(listId, listDto.getFilters());
    }

    @DeleteMapping("/{listId}")
    public ListDto deleteList(@PathVariable Long listId) {
        return listService.deleteById(listId);
    }

}
