package com.example.booking_service.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;

@Data
public class TicketDto implements Serializable {
    private Long id;
    private String name;
    private LocalDate creationDate;
    private Coordinates coordinates;
    private Integer price;
    private TicketType type;
    private VenueDto venue;
    private EventDto event;
    private Long personId;
}