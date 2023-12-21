package com.example.spring_service.dto;

import com.example.spring_service.model.Coordinates;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TicketDto {
    private Long id;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate creationDate;
    private Coordinates coordinates;
    private Integer price;
    private String type;
    private VenueDto venue;
    private EventDto event;
    private Long personId;
}
