package com.example.booking_service.client;


import com.example.booking_service.dto.TicketDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(value = "ticket-service", url = "http://localhost:8080/api/v1/tickets")
public interface TicketClient {

    @GetMapping
    List<TicketDto> getTickets();

    @DeleteMapping(value = "/{ticketId}")
    void deleteTicket(@PathVariable Long ticketId);

    @PutMapping(value = "/{ticketId}")
    void updateTicket(@PathVariable Long ticketId, @RequestBody TicketDto ticketDto);
}
