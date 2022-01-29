package com.example.documentsystem.services;

import com.example.documentsystem.entities.OrganizationEntity;

import java.util.List;

public interface OrganizationService {
    List<OrganizationEntity> findAll();

    OrganizationEntity findById(Long id);

    OrganizationEntity create(OrganizationEntity org);

    OrganizationEntity update(OrganizationEntity org);

    OrganizationEntity deleteById(Long id);
}
