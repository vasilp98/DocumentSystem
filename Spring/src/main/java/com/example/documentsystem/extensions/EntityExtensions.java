package com.example.documentsystem.extensions;

import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.entities.ListEntity;
import com.example.documentsystem.entities.PermissionEntity;
import com.example.documentsystem.entities.UserEntity;
import com.example.documentsystem.models.Document;
import com.example.documentsystem.models.DocumentUserFields;
import com.example.documentsystem.models.ListDto;
import com.example.documentsystem.models.filter.Filter;
import com.example.documentsystem.models.filter.FilterList;
import com.example.documentsystem.models.permission.Permission;
import com.example.documentsystem.models.permission.PermissionDto;

import javax.validation.ValidationException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EntityExtensions {
    public static Document toDocument(DocumentEntity documentEntity) {
        return new Document(
                documentEntity.getId(),
                documentEntity.getStoreDate(),
                documentEntity.getStoreUser(),
                documentEntity.getModifyDate(),
                documentEntity.getModifyUser(),
                documentEntity.getSize(),
                documentEntity.getFilesCount(),
                new DocumentUserFields(
                        documentEntity.getName(),
                        documentEntity.getDocumentType(),
                        documentEntity.getCompany(),
                        documentEntity.getDate(),
                        documentEntity.getContact(),
                        documentEntity.getStatus(),
                        documentEntity.getAmount(),
                        documentEntity.getNumber()
                )
        );
    }

    public static PermissionDto toDto(PermissionEntity permissionEntity) {
        return new PermissionDto(
                permissionEntity.getId(),
                permissionEntity.getFolderId(),
                permissionEntity.getUsers().stream().map(UserEntity::getId).collect(Collectors.toList()),
                permissionEntity.getArea(),
                new ArrayList<>(permissionEntity.getPermissions()),
                deserializeToFilters(permissionEntity.getFilter())
        );
    }

    public static ListDto toDto(ListEntity listEntity) {
        return new ListDto(
                listEntity.getId(),
                listEntity.getFolderId(),
                listEntity.getOwnerId(),
                listEntity.getName(),
                deserializeToFilters(listEntity.getFilters())
        );
    }

    public static List<Filter> deserializeToFilters(String filters) {
        try {
            StringReader reader = new StringReader(filters);
            JAXBContext context = JAXBContext.newInstance(FilterList.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return ((FilterList) unmarshaller.unmarshal(reader)).getFilters();
        } catch (JAXBException exception) {
            throw new RuntimeException(exception.getMessage());
        }
    }

    public static String serialize(List<Filter> filters) {
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
}
