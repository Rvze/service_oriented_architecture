package com.example.spring_service.dto;

import com.example.spring_service.model.VenueType;
import lombok.Data;

@Data
public class VenueDto {
    private String name;
    private Integer capacity;
    private VenueType type;
}
