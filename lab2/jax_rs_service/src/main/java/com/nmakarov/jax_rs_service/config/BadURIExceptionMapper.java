package com.nmakarov.jax_rs_service.config;

import com.nmakarov.jax_rs_service.dto.ErrorResponse;

import javax.ws.rs.Consumes;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BadURIExceptionMapper implements ExceptionMapper<NotFoundException> {


    public Response toResponse(NotFoundException exception) {
        return Response.ok(new ErrorResponse("Incorrect path", Response.Status.NOT_FOUND.toString()))
                .status(Response.Status.NOT_FOUND)
                .type("application/json").
                build();
    }
}