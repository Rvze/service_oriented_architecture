package com.nmakarov.jax_rs_service.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class EventDto implements Serializable {
    private Long id;
    private String name;
    private List<TicketDto> tickets;

}
