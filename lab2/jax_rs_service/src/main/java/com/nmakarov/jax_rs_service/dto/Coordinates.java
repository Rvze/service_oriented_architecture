package com.nmakarov.jax_rs_service.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class Coordinates implements Serializable {
    private long x;
    private Integer y;
}
