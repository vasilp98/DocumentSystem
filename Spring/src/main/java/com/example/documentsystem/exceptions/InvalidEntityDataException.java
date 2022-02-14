package com.example.documentsystem.exceptions;

public class InvalidEntityDataException extends RuntimeException {
    public InvalidEntityDataException(String message) {
        super(message);
    }
}
