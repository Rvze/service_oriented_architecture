package com.nmakarov.jax_rs_service.service;

import com.nmakarov.jax_rs_service.client.TicketClient;
import com.nmakarov.jax_rs_service.dto.EventDto;
import com.nmakarov.jax_rs_service.dto.TicketDto;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class BookingService {
    private final TicketClient ticketClient = new TicketClient();

    public void deleteEvent(Long eventId) {
        List<TicketDto> tickets = ticketClient.getTickets();
        tickets.forEach(t -> {
            EventDto event = t.getEvent();
            if (Objects.equals(event.getId(), eventId)) {
                ticketClient.deleteTicket(t.getId());
            }
        });
    }

    public List<TicketDto> findTickets() {
        return ticketClient.getTickets();
    }

    public void deletePersonBookings(Long personId) {
        List<TicketDto> tickets = ticketClient.getTickets();
        tickets.forEach(t -> {
            if (Objects.equals(t.getPersonId(), personId)) {
                t.setPersonId(null);
                ticketClient.updateTicket(t);
            }
        });
    }

}
