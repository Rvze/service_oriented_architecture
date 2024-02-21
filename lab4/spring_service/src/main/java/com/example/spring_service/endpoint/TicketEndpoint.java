package com.example.spring_service.endpoint;

import com.example.spring_service.dto.*;
import com.example.spring_service.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class TicketEndpoint {
    private static final String NAMESPACE_URI = "com/example/spring_service/dto";

    private final TicketService ticketService;

    @Autowired
    public TicketEndpoint(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getTicketByIdRequest")
    @ResponsePayload
    public TicketDtoResponse findById(@RequestPayload GetTicketByIdRequest ticketId) {
        return ticketService.findById(ticketId.getId());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "ticketDtoRequest")
    @ResponsePayload
    public TicketDtoResponse createTicket(@RequestPayload TicketDtoRequest ticketDto) {
        return ticketService.createTicket(ticketDto);
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateTicketRequest")
    @ResponsePayload
    public TicketDtoResponse update(@RequestPayload UpdateTicketRequest updateTicketRequest) {
        return ticketService.updateTicket(updateTicketRequest.getId(), updateTicketRequest.getTicketDto());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteTicketByIdRequest")
    public void delete(@RequestPayload DeleteTicketByIdRequest ticketId) {
        ticketService.deleteById(ticketId.getId());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findAllTicketsRequest")
    @ResponsePayload
    public TicketsDtoResponse findAll(@RequestPayload FindAllTicketsRequest findAllTicketsRequest) {
        return ticketService.findAll(findAllTicketsRequest.getPage(), findAllTicketsRequest.getPageSize(),
                findAllTicketsRequest.getSortParams(), findAllTicketsRequest.getFilterParams());
    }
}
