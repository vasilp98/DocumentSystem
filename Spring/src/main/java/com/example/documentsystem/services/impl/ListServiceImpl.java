package com.example.documentsystem.services.impl;

import com.example.documentsystem.dao.ListRepository;
import com.example.documentsystem.entities.ListEntity;
import com.example.documentsystem.models.auditing.AuditedField;
import com.example.documentsystem.models.auditing.AuditedFieldList;
import com.example.documentsystem.models.filter.Filter;
import com.example.documentsystem.models.filter.FilterList;
import com.example.documentsystem.services.ListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.List;

@Service
@Transactional
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
        return listRepository.save(new ListEntity(folderId, getFilterAsString(filters)));
    }

    @Override
    public ListEntity update(Long id, List<Filter> filters) {
        ListEntity listEntity = findById(id);
        listEntity.setFilters(getFilterAsString(filters));

        return listRepository.save(listEntity);
    }

    @Override
    public ListEntity deleteById(Long id) {
        ListEntity old = findById(id);
        listRepository.deleteById(id);
        return old;
    }

    private String getFilterAsString(List<Filter> filters) {
        try {
            StringWriter writer = new StringWriter();
            JAXBContext context = JAXBContext.newInstance(FilterList.class);

            Marshaller marshaller = context.createMarshaller();
            marshaller.marshal(new FilterList(filters), writer);

            return writer.toString();
        } catch (JAXBException exception) {
            throw new ValidationException(exception.getMessage());
        }
    }

    private List<Filter> getFilters(String filters) {
        try {
            StringReader reader = new StringReader(filters);
            JAXBContext context = JAXBContext.newInstance(FilterList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return ((FilterList) unmarshaller.unmarshal(reader)).getFilters();
        } catch (JAXBException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }
}
