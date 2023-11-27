package com.example.spring_service.controller;

import com.example.spring_service.dto.VenueDto;
import com.example.spring_service.model.VenueType;
import com.example.spring_service.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class OperationsController {
    private final TicketService ticketService;

    @DeleteMapping("/venue")
    public ResponseEntity<Void> deleteWithVenue(@RequestBody VenueDto venueDto) {
        ticketService.deleteWithVenue(venueDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/venue/amount")
    public ResponseEntity<Integer> getWithVenue(@RequestParam String name, @RequestParam Integer capacity, @RequestParam VenueType type) {
        Integer amount = ticketService.getWithVenue(name, capacity, type);
        return ResponseEntity.ok(amount);
    }

    @GetMapping("/prices/unique")
    public ResponseEntity<List<Integer>> getUniquePrices() {
        List<Integer> uniquePrices = ticketService.getUniquePrices();
        return ResponseEntity.ok(uniquePrices);
    }
}
