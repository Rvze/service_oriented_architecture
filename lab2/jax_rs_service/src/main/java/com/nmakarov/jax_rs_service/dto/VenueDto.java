package com.nmakarov.jax_rs_service.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class VenueDto implements Serializable {
    private String name;
    private Integer capacity;
    private VenueType type;

}
