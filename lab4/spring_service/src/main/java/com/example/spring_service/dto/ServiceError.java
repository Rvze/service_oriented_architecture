package com.example.spring_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(namespace = "ServiceError", propOrder = {
        "code",
        "message"
})
@Data
@AllArgsConstructor
public class ServiceError {
    private String code;
    private String message;
}
