package com.example.spring_service.exception;

public class IncorrectFilterException extends RuntimeException {
    public IncorrectFilterException(String message) {
        super(message);
    }
}
