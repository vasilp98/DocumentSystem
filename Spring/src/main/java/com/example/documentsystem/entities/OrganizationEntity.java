package com.example.documentsystem.entities;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "organizations")
@Data
public class OrganizationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
