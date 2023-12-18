package com.example.spring_service.configuration;

import com.example.spring_service.dto.Error;
import com.example.spring_service.exception.TicketNotFoundException;
import com.example.spring_service.model.ErrorResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Set<Error>> handleConstraintViolationException(ConstraintViolationException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fromConstraintViolationException(e));
    }

    @ExceptionHandler(TicketNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTicketNotFound(TicketNotFoundException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage(), 500));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handeAll(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getMessage(), 500));
    }


    private Set<Error> fromConstraintViolationException(ConstraintViolationException e) {
        Set<Error> errors = new HashSet<>();
        e.getConstraintViolations().forEach(it -> errors.add(
                new Error("ILLEGAL_ARGUMENT_PROVIDED", it.getMessage())));
        return errors;
    }

}
