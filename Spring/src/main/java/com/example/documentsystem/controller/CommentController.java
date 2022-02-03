package com.example.documentsystem.controller;

import com.example.documentsystem.entities.CommentEntity;
import com.example.documentsystem.models.Comment;
import com.example.documentsystem.services.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {
    private final CommentService commentService;

    @Autowired
    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @GetMapping("/{documentId}")
    public List<CommentEntity> getAll(@PathVariable Long documentId) {
        return commentService.findAllByDocumentId(documentId);
    }

    @PostMapping("{documentId}")
    public CommentEntity create(@PathVariable Long documentId, @RequestBody Comment commentDto) {
        return this.commentService.create(new CommentEntity(commentDto.getDocumentId(), commentDto.getCreatedUser(), commentDto.getContent()));
    }
}
