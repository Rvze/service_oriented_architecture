package com.example.booking_service.controller;

import com.example.booking_service.dto.TicketDto;
import com.example.booking_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/booking")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @DeleteMapping("/event/{event-id}/cancel")
    public ResponseEntity<Void> deleteEvent(@PathVariable("event-id") Long eventId) {
        bookingService.deleteEvent(eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/person/{person-id}/cancel")
    public ResponseEntity<Void> deletePerson(@PathVariable("person-id") Long personId) {
        bookingService.deletePersonBookings(personId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/tickets")
    public ResponseEntity<List<TicketDto>> findTickets() {
        var tickets = bookingService.findTickets();
        return ResponseEntity.ok(tickets);
    }

}
