package com.itec3860.ipaapi;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import jakarta.persistence.PersistenceException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<?> handlePersistenceException(PersistenceException e) {

        Throwable rootCause = e.getCause();

        if (rootCause instanceof java.sql.SQLIntegrityConstraintViolationException) {

            return new ResponseEntity<>("Constraint violation: " + rootCause.getMessage(), HttpStatus.CONFLICT);

        }

        return new ResponseEntity<>("Database error: " + e.getMessage(), HttpStatus.CONFLICT);

    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneralException(Exception e) {

        return new ResponseEntity<>("An unexpected error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);

    }
}

