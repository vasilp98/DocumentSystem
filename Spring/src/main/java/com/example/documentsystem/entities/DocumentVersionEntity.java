package com.example.documentsystem.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "versions")
@Data
public class DocumentVersionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
