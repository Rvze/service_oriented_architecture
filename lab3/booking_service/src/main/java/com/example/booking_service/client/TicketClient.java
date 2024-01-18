package com.example.booking_service.client;


import com.example.booking_service.dto.TicketDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(url = "https://localhost:8090/api/v1/tickets")
public interface TicketClient {

    @RequestMapping(method = RequestMethod.GET)
    List<TicketDto> getTickets();

    @RequestMapping(method = RequestMethod.DELETE, value = "/{ticketId}")
    void deleteTicket(@PathVariable Long ticketId);

    @RequestMapping(method = RequestMethod.PUT, value = "/{ticketId}")
    void updateTicket(@PathVariable Long ticketId, @RequestBody TicketDto ticketDto);
}
