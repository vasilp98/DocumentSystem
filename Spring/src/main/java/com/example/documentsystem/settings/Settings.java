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

        return fieldType;
    }
}
