package com.nmakarov.jax_rs_service.config;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@ApplicationScoped
public class ObjectMapperConfig {
    @Produces(MediaType.APPLICATION_JSON)
    public ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }
}
