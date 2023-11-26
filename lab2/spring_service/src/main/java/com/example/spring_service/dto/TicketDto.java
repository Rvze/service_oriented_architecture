package com.example.spring_service.dto;

import com.example.spring_service.model.Coordinates;
import com.example.spring_service.model.TicketType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDto {
    private Long id;
    private String name;
    private LocalDateTime creationDate;
    private Coordinates coordinates;
    private Integer price;
    private TicketType type;
    private VenueDto venue;
    private EventDto event;
    private Long personId;
}
