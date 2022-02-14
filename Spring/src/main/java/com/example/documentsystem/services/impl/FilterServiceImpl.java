package com.example.documentsystem.services.impl;

import com.example.documentsystem.entities.DocumentEntity;
import com.example.documentsystem.exceptions.InvalidOperationException;
import com.example.documentsystem.models.filter.Filter;
import com.example.documentsystem.models.filter.Operation;
import com.example.documentsystem.services.FilterService;
import com.example.documentsystem.settings.FieldType;
import com.example.documentsystem.settings.Settings;
import org.springframework.stereotype.Service;

import javax.validation.ValidationException;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FilterServiceImpl implements FilterService {
    @Override
    public List<DocumentEntity> filterDocuments(List<DocumentEntity> documentEntities, List<Filter> filters) {
        return documentEntities.stream().filter(de -> checkDocument(de, filters)).collect(Collectors.toList());
    }

    @Override
    public boolean checkDocument(DocumentEntity documentEntity, List<Filter> filters) {
        Map<String, FieldType> fieldTypes = Settings.getFieldTypes();

        for (Filter filter: filters) {
            try {
                if (filter.getField() == null && filter.getOperation() == null && filter.getValue() == null)
                    return true;

                Field field = documentEntity.getClass().getDeclaredField(filter.getField());
                field.setAccessible(true);
                Object value = field.get(documentEntity);

                boolean isValid = false;
                switch (fieldTypes.get(filter.getField())) {
                    case TEXT:
                        isValid = checkStringValue(filter, (String)value);
                        break;
                    case NUMERIC:
                        isValid = checkNumericValue(filter, (Integer)value);
                        break;
                    case DATE:
                        isValid = checkDateValue(filter, (LocalDate)value);
                        break;
                }

                if (isValid == false)
                    return false;

            } catch (NoSuchFieldException exception) {
                throw new ValidationException(String.format("Field with name='%s' is not valid", filter.getField()));
            } catch (IllegalAccessException illegalAccessException) {
                throw new RuntimeException(illegalAccessException.getMessage());
            } catch (NullPointerException exception) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void validateFilter(Filter filter) {
        if (filter == null)
            return;

        Map<String, FieldType> fieldTypes = Settings.getFieldTypes();

            switch (fieldTypes.get(filter.getField())) {
                case TEXT:
                    validateStringFilter(filter);
                    break;
                case NUMERIC:
                    validateNumericFilter(filter);
                    break;
                case DATE:
                    validateDateFilter(filter);
                    break;
            }
    }

    private void validateStringFilter(Filter filter) {
        switch (filter.getOperation()) {
            case BEFORE:
                throw new InvalidOperationException("Operation 'Before' is not valid for text fields");
            case AFTER:
                throw new InvalidOperationException("Operation 'After' is not valid for text fields");
        }
    }

    private void validateNumericFilter(Filter filter) {
        if (filter.getOperation() == Operation.CONTAINS) {
            throw new InvalidOperationException("Operation 'Contains' is not valid for numeric fields");
        }

        try {
            Integer.parseInt(filter.getValue());
        } catch (NumberFormatException e) {
            throw new ValidationException(String.format("'%s' is not valid number!", filter.getValue()));
        }
    }

    private void validateDateFilter(Filter filter) {
        if (filter.getOperation() == Operation.CONTAINS) {
            throw new InvalidOperationException("Operation 'Contains' is not valid for date fields");
        }

        try {
            LocalDate.parse(filter.getValue());
        } catch (DateTimeParseException e) {
            throw new ValidationException(String.format("'%s' is not valid date!", filter.getValue()));
        }
    }

    private boolean checkStringValue(Filter filter, String value) {
        switch (filter.getOperation()) {
            case EQUAL:
                return filter.getValue().equals(value);
            case NOT_EQUAL:
                return !filter.getValue().equals(value);
            case CONTAINS:
                return value.contains(filter.getValue());
            case BEFORE:
                throw new InvalidOperationException("Operation 'Before' is not valid for text fields");
            case AFTER:
                throw new InvalidOperationException("Operation 'After' is not valid for text fields");
        }

        return false;
    }

    private boolean checkNumericValue(Filter filter, Integer value) {
        switch (filter.getOperation()) {
            case EQUAL:
                return Integer.parseInt(filter.getValue()) == value;
            case NOT_EQUAL:
                return Integer.parseInt(filter.getValue()) != value;
            case CONTAINS:
                throw new InvalidOperationException("Operation 'Contains' is not valid for numeric fields");
            case BEFORE:
                return value < Integer.parseInt(filter.getValue());
            case AFTER:
                return value > Integer.parseInt(filter.getValue());
        }

        return false;
    }

    private boolean checkDateValue(Filter filter, LocalDate value) {
        LocalDate date = LocalDate.parse(filter.getValue());

        switch (filter.getOperation()) {
            case EQUAL:
                return date.equals(value);
            case NOT_EQUAL:
                return !date.equals(value);
            case CONTAINS:
                throw new InvalidOperationException("Operation 'Contains' is not valid for date fields");
            case BEFORE:
                return value.isBefore(date);
            case AFTER:
                return value.isAfter(date);
        }

        return false;
    }
}
