package com.example.documentsystem.settings;

import java.util.HashMap;
import java.util.Map;

public class Settings {
    public static Map<String, FieldType> getFieldTypes() {
        Map<String, FieldType> fieldType = new HashMap<>();
        fieldType.put("name", FieldType.TEXT);
        fieldType.put("storeDate", FieldType.DATE);
        fieldType.put("storeUser", FieldType.TEXT);
        fieldType.put("modifyDate", FieldType.DATE);
        fieldType.put("modifyUser", FieldType.TEXT);
        fieldType.put("documentType", FieldType.TEXT);
        fieldType.put("company", FieldType.TEXT);
        fieldType.put("date", FieldType.DATE);
        fieldType.put("contact", FieldType.TEXT);
        fieldType.put("status", FieldType.TEXT);
        fieldType.put("amount", FieldType.NUMERIC);
        fieldType.put("number", FieldType.NUMERIC);

        return fieldType;
    }
}
