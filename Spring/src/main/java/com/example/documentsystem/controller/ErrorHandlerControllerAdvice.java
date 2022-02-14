package com.example.documentsystem.controller;

import com.example.documentsystem.exceptions.*;
import com.example.documentsystem.models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
@Slf4j
public class ErrorHandlerControllerAdvice {
    @ExceptionHandler({EntityNotFoundException.class, FileNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleEntityNotFound(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage())
        );
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, InvalidOperationException.class, ValidationException.class})
    public ResponseEntity<ErrorResponse> handleBadRequest(Exception ex) {
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage())
        );
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(Throwable ex) throws Throwable {
        while(!(ex instanceof SQLIntegrityConstraintViolationException) && ex.getCause() != null) {
            ex = ex.getCause();
        }
        if(ex instanceof SQLIntegrityConstraintViolationException) {
            return ResponseEntity.badRequest().body(
                    new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Invalid constraint: " + ex.getMessage())
            );
        } else {
            throw ex;
        }
    }

    @ExceptionHandler({UnauthorizedException.class, WrongPasswordException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationException(Exception ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON).body(
                new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage())
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(PermissionException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ErrorResponse(HttpStatus.FORBIDDEN.value(), ex.getMessage())
        );
    }
}
