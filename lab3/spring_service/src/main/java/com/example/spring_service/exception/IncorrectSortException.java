package com.example.spring_service.exception;

public class IncorrectSortException extends RuntimeException {
    public IncorrectSortException(String message) {
        super(message);
    }
}
