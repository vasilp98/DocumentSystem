package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.OrganizationRepository;
import com.example.documentsystem.entities.OrganizationEntity;
import com.example.documentsystem.services.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
public class OrganizationServiceImpl implements OrganizationService {
    private final OrganizationRepository repo;

    @Autowired
    public OrganizationServiceImpl(OrganizationRepository repo) {
        this.repo = repo;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationEntity> findAll() {
        return repo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public OrganizationEntity findById(Long organizationId) {
        return repo.findById(organizationId).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("Organization with ID=%s not found.", organizationId)));
    }

    @Override
    public OrganizationEntity create(OrganizationEntity organization) {
        return repo.save(organization);
    }

    @Override
    public OrganizationEntity update(OrganizationEntity organization) {
        return repo.save(organization);
    }

    @Override
    public OrganizationEntity deleteById(Long organizationId) {
        OrganizationEntity old = findById(organizationId);
        repo.deleteById(organizationId);
        return old;
    }
}
