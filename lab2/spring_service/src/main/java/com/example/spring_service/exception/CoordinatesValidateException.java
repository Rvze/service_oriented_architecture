package com.example.spring_service.exception;

public class CoordinatesValidateException extends RuntimeException{
    public CoordinatesValidateException(String message) {
        super(message);
    }
}
