package com.example.spring_service.controller;

import com.example.spring_service.dto.TicketDto;
import com.example.spring_service.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@RequestBody TicketDto ticketDto) {
        TicketDto created = ticketService.createTicket(ticketDto);
        return ResponseEntity.ok(created);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<TicketDto> findById(@PathVariable("id") Long ticketId) {
        TicketDto find = ticketService.findById(ticketId);
        return ResponseEntity.ok(find);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<TicketDto> update(@RequestBody TicketDto ticketDto, @PathVariable("id") Long ticketId) {
        TicketDto updated = ticketService.updateTicket(ticketId, ticketDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long ticketId) {
        ticketService.deleteById(ticketId);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> findAll(@RequestParam(name = "page", defaultValue = "0") Integer page, @RequestParam(value = "page_size", defaultValue = "10") Integer pageSize,
                                                   @RequestParam(name = "sort", required = false) String sort, @RequestParam(name = "filter", required = false) List<String> filter) {
        List<TicketDto> ticketList = ticketService.findAll(page, pageSize, sort, filter);
        return ResponseEntity.ok(ticketList);
    }


}
