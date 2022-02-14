package com.example.documentsystem.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "lists")
@Data
@NoArgsConstructor
public class ListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
    @JoinColumn(name = "FOLDER_ID", insertable = false, updatable = false)
    @OnDelete( action = OnDeleteAction.CASCADE )
    private FolderEntity folder;

    @Column(name = "FOLDER_ID")
    private Long folderId;

    private Long ownerId;

    private String name;

    private String filters;

    public ListEntity(Long folderId, Long ownerId, String name, String filters) {
        this.folderId = folderId;
        this.filters = filters;
        this.ownerId = ownerId;
        this.name = name;
    }
}
