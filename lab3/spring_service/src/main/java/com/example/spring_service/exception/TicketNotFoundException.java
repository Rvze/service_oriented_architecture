package com.example.spring_service.exception;

public class TicketNotFoundException extends RuntimeException {
    public TicketNotFoundException(String msg) {
        super(msg);
    }
}
