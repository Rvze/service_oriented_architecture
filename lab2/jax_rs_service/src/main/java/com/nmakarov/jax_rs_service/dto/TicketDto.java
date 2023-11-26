package com.nmakarov.jax_rs_service.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class TicketDto implements Serializable {
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
