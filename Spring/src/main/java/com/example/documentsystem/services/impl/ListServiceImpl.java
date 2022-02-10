package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.ListRepository;
import com.example.documentsystem.entities.ListEntity;
import com.example.documentsystem.extensions.EntityExtensions;
import com.example.documentsystem.models.filter.Filter;
import com.example.documentsystem.services.ListService;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@ExtensionMethod(EntityExtensions.class)
public class ListServiceImpl implements ListService {
    private ListRepository listRepository;

    public ListServiceImpl(ListRepository listRepository) {
        this.listRepository = listRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ListEntity> findAllInFolder(Long folderId) {
        return listRepository.findAllByFolderId(folderId);
    }

    @Override
    @Transactional(readOnly = true)
    public ListEntity findById(Long id) {
        return listRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("List with ID=%s not found.", id)));
    }

    @Override
    public ListEntity create(Long folderId, List<Filter> filters) {
        return listRepository.save(new ListEntity(folderId, filters.serialize()));
    }

    @Override
    public ListEntity update(Long id, List<Filter> filters) {
        ListEntity listEntity = findById(id);
        listEntity.setFilters(filters.serialize());

        return listRepository.save(listEntity);
    }

    @Override
    public ListEntity deleteById(Long id) {
        ListEntity old = findById(id);
        listRepository.deleteById(id);
        return old;
    }
}
