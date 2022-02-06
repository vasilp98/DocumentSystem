package com.example.documentsystem.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "DOCUMENT_ID", insertable = false, updatable = false)
    private DocumentEntity document;

    @Column(name = "DOCUMENT_ID")
    private Long documentId;

    @NotNull
    private String createdUser;

    private String content;

    @NotNull
    private LocalDateTime created = LocalDateTime.now();

    @NotNull
    private LocalDateTime modified = LocalDateTime.now();

    public CommentEntity(Long documentId, String createdUser, String content) {
        this.documentId = documentId;
        this.createdUser = createdUser;
        this.content = content;
    }
}
