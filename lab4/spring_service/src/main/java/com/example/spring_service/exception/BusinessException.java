package com.example.spring_service.exception;

import com.example.spring_service.dto.ServiceError;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException {

    private ServiceError serviceError;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, ServiceError serviceError) {
        super(message);
        this.serviceError = serviceError;
    }
}
