package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.ListRepository;
import com.example.documentsystem.entities.ListEntity;
import com.example.documentsystem.extensions.EntityExtensions;
import com.example.documentsystem.models.ListDto;
import com.example.documentsystem.models.filter.Filter;
import com.example.documentsystem.services.ListService;
import lombok.experimental.ExtensionMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<ListDto> findAllInFolder(Long folderId) {
        return listRepository.findAllByFolderId(folderId).stream().map(l -> l.toDto()).collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ListDto findById(Long id) {
        return listRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(
                        String.format("List with ID=%s not found.", id))).toDto();
    }

    @Override
    public ListDto create(Long folderId, List<Filter> filters) {
        return listRepository.save(new ListEntity(folderId, filters.serialize())).toDto();
    }

    @Override
    public ListDto update(Long id, List<Filter> filters) {
        ListEntity listEntity = listRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("List with ID=%s not found.", id)));

        listEntity.setFilters(filters.serialize());

        return listRepository.save(listEntity).toDto();
    }

    @Override
    public ListDto deleteById(Long id) {
        ListDto old = findById(id);
        listRepository.deleteById(id);
        return old;
    }
}
