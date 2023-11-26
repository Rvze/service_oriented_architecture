package com.nmakarov.jax_rs_service.controller;

import com.nmakarov.jax_rs_service.service.BookingService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/booking")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookingController {
    @Inject
    private BookingService bookingService;

    @DELETE
    @Path("/event/{event-id}/cancel")
    public Response deleteEvent(@PathParam("event-id") Long eventId) {
        bookingService.deleteEvent(eventId);
        return Response.ok().build();
    }

    @DELETE
    @Path("/person/{person-id}/cancel")
    public Response deletePerson(@PathParam("person-id") Long personId) {
        bookingService.deletePersonBookings(personId);
        return Response.ok().build();
    }

    @GET
    @Path("/events")
    public Response findTickets() {
        var tickets = bookingService.findTickets();
        return Response.ok(tickets).build();
    }
}
