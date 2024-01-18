package com.example.booking_service.service;

import com.example.booking_service.client.TicketClient;
import com.example.booking_service.dto.EventDto;
import com.example.booking_service.dto.TicketDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingService {
    private final TicketClient ticketClient;

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
                ticketClient.updateTicket(t.getId(), t);
            }
        });
    }
}
