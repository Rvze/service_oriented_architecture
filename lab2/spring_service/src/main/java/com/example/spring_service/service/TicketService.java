package com.example.spring_service.service;

import com.example.spring_service.dto.TicketDto;
import com.example.spring_service.dto.VenueDto;
import com.example.spring_service.exception.IncorrectFilterException;
import com.example.spring_service.exception.IncorrectSortException;
import com.example.spring_service.exception.TicketNotFoundException;
import com.example.spring_service.mapper.TicketMapper;
import com.example.spring_service.mapper.VenueMapper;
import com.example.spring_service.model.Ticket;
import com.example.spring_service.model.Venue;
import com.example.spring_service.model.VenueType;
import com.example.spring_service.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final VenueMapper venueMapper;
    private final VenueService venueService;

    private static final String TICKET_NOT_FOUND_MSG = "Ticket not found by id = %d";

    @Transactional
    public TicketDto createTicket(TicketDto ticketDto) {
        Ticket mappedTicket = ticketMapper.toEntity(ticketDto);
        return ticketMapper.toDto(ticketRepository.save(mappedTicket));
    }

    public TicketDto findById(Long ticketId) {
        Ticket findTicket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new TicketNotFoundException(String.format(TICKET_NOT_FOUND_MSG, ticketId)));
        return ticketMapper.toDto(findTicket);
    }

    @Transactional
    public TicketDto updateTicket(Long ticketId, TicketDto ticketDto) {
        Ticket targetTicket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new TicketNotFoundException(String.format(TICKET_NOT_FOUND_MSG, ticketId)));
        ticketMapper.update(targetTicket, ticketDto);
        return ticketMapper.toDto(targetTicket);
    }

    public void deleteById(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new TicketNotFoundException(String.format(TICKET_NOT_FOUND_MSG, ticketId)));
        ticketRepository.delete(ticket);
    }

    public List<TicketDto> findAll(Integer page, Integer pageSize, String sort, List<String> filter) {
        if (pageSize == null) {
            return ticketRepository.findAll().stream().map(ticketMapper::toDto).toList();
        }
        Sort sortBy = Sort.unsorted();
        if (sort != null) {
            sortBy = resolveSort(sort);
        }
        Pageable pageable = PageRequest.of(page, pageSize, sortBy);
        Page<Ticket> pages = ticketRepository.findAll(pageable);
        List<Triple<String, String, String>> resolvedFilters = new ArrayList<>();
        if (filter != null && !filter.isEmpty())
            filter.forEach(f -> resolvedFilters.add(resolveFilter(f)));

        return pages.getContent().stream()
                .filter(t -> filter(t, resolvedFilters))
                .map(ticketMapper::toDto)
                .toList();
    }

    @Transactional
    public void deleteWithVenue(VenueDto venueDto) {
        List<Ticket> tickets = ticketRepository.findAll();
        Venue venue = venueMapper.toEntity(venueDto);
        List<Ticket> filteredTickets = tickets.stream()
                .filter(t -> t.getVenue().equals(venue))
                .toList();
        ticketRepository.deleteAll(filteredTickets);
    }

    public Integer getWithVenue(String name, Integer capacity, VenueType type) {
        Venue specifiedVenue = Venue.builder().name(name).capacity(capacity).type(type).build();
        List<Ticket> tickets = ticketRepository.findAll();
        long amount = tickets.stream().filter(ticket -> {
            Venue venue = ticket.getVenue();
            return venue.equals(specifiedVenue);
        }).count();
        return (int) amount;
    }

    public List<Integer> getUniquePrices() {
        List<Ticket> tickets = ticketRepository.findAll();
        Map<Integer, Long> priceCounts = tickets.stream()
                .map(Ticket::getPrice)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return priceCounts.entrySet().stream()
                .filter(entry -> entry.getValue() == 1)
                .map(Map.Entry::getKey)
                .toList();
    }

    private Triple<String, String, String> resolveFilter(String filter) {
        String[] split = filter.split("_");
        if (split.length > 3) {
            throw new IncorrectFilterException(String.format("The filter cannot consist of more than 3 arguments: %s", filter));
        } else if (split.length == 0) {
            throw new IncorrectFilterException("Incorrect filter format. Look at the documentation");
        }
        return Triple.of(split[0], split[1], split[2]);
    }

    private Sort resolveSort(String sort) {
        String[] split = sort.split("_");
        if (split.length != 2) {
            throw new IncorrectSortException(String.format("The sorting must consist of exactly 2 arguments : %s", sort));
        }
        if (split[1].equalsIgnoreCase("asc")) {
            return Sort.by(Sort.Order.asc(split[0]));
        } else {
            return Sort.by(Sort.Order.desc(split[0]));
        }
    }

    private boolean filter(Ticket ticket, List<Triple<String, String, String>> resolvedFilters) {
        AtomicBoolean filterResult = new AtomicBoolean(true);
        resolvedFilters.forEach(resolvedFilter -> {
            String field = resolvedFilter.getLeft().toLowerCase();
            String operation = resolvedFilter.getMiddle();
            String value = resolvedFilter.getRight();

            switch (field) {
                case "name" ->
                        filterResult.set(filterResult.get() && strOperationFilter(ticket::getName, operation, value));
                case "price" ->
                        filterResult.set(filterResult.get() && numOperationFilter(ticket::getPrice, operation, Integer.valueOf(value)));

            }
        });
        return filterResult.get();
    }

    private boolean strOperationFilter(Supplier<String> argument, String operation, String value) {
        return switch (operation) {
            case "eq" -> argument.get().equals(value);
            case "neq" -> !argument.get().equals(value);
            case "more" -> value.length() >= argument.get().length();
            case "less" -> value.length() < argument.get().length();
            default -> throw new IllegalStateException("Unexpected value: " + operation);
        };
    }

    private boolean numOperationFilter(Supplier<Number> argument, String operation, Number value) {
        return switch (operation) {
            case "eq" -> Objects.equals(argument.get(), value);
            case "neq" -> !Objects.equals(argument.get(), value);
            case "more" -> argument.get().intValue() > value.intValue();
            case "less" -> argument.get().intValue() < value.intValue();
            default -> throw new IllegalStateException("Unexpected value: " + operation);
        };
    }

}
