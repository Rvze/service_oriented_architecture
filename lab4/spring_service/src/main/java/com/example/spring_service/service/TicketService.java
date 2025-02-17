package com.example.spring_service.service;

import com.example.spring_service.dto.*;
import com.example.spring_service.exception.*;
import com.example.spring_service.mapper.TicketMapper;
import com.example.spring_service.mapper.VenueMapper;
import com.example.spring_service.model.FilterOperations;
import com.example.spring_service.model.Ticket;
import com.example.spring_service.model.Venue;
import com.example.spring_service.repository.TicketRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
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
    public CreateTicketResponse createTicket(CreateTicketRequest ticketDto) {
        CreateTicketResponse response = new CreateTicketResponse();
        ticketDto.setType(ticketDto.getType().toUpperCase());
        Ticket mappedTicket = ticketMapper.toEntity(ticketDto);
        var coordinates = mappedTicket.getCoordinates();
        if (coordinates.getX() == null)
            throw new CoordinatesValidateException("Coordinate x must be not null");
        if (coordinates.getY() < -734) {
            throw new CoordinatesValidateException("Coordinate y must be greater than -734");
        }
        try {
            response.setTicketDto(ticketMapper.toDto(ticketRepository.save(mappedTicket)));
        } catch (ConstraintViolationException constraintViolationException) {
            String message = constraintViolationException.getConstraintViolations().stream()
                    .map(ConstraintViolation::getMessage)
                    .collect(Collectors.joining(", "));
            throw new BusinessException(message,
                    new ServiceError("404", message));
        }
        return response;
    }

    public GetTicketByIdResponse findById(Long ticketId) {
        Ticket findTicket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new BusinessException(String.format(TICKET_NOT_FOUND_MSG, ticketId), new ServiceError("404", String.format(TICKET_NOT_FOUND_MSG, ticketId))));
        GetTicketByIdResponse response = new GetTicketByIdResponse();
        response.setTicketDto(ticketMapper.toDto(findTicket));
        return response;
    }

    @Transactional
    public UpdateTicketResponse updateTicket(Long ticketId, TicketDto ticketDto) {
        ticketDto.setType(ticketDto.getType().toUpperCase());
        if (ticketDto.getPrice() <= 0) {
            throw new BusinessException("Ticket price must be higher than 0");
        }
        Ticket targetTicket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new BusinessException(String.format(TICKET_NOT_FOUND_MSG, ticketId), new ServiceError("404", TICKET_NOT_FOUND_MSG)));
        ticketMapper.update(targetTicket, ticketDto);
        UpdateTicketResponse response = new UpdateTicketResponse();
        response.setTicketDto(ticketMapper.toDto(targetTicket));
        return response;
    }

    public void deleteById(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() ->
                new BusinessException(String.format(TICKET_NOT_FOUND_MSG, ticketId), new ServiceError("404", TICKET_NOT_FOUND_MSG)));
        ticketRepository.delete(ticket);
    }

    public GetAllTicketsResponse findAll(GetAllTicketsRequest getAllTicketsRequest) {
        GetAllTicketsResponse ticketsDto = new GetAllTicketsResponse();
        if (getAllTicketsRequest.getPageSize() == 0) {
            ticketsDto.getTickets().addAll(ticketRepository.findAll().stream().map(ticketMapper::toDto).toList());
            return ticketsDto;
        }
        Sort sortBy = Sort.unsorted();
        if (getAllTicketsRequest.getSortParams() != null && !getAllTicketsRequest.getSortParams().isEmpty()) {
            sortBy = resolveSort(getAllTicketsRequest.getSortParams());
        }
        Pageable pageable = PageRequest.of(getAllTicketsRequest.getPage(), getAllTicketsRequest.getPageSize(), sortBy);
        Page<Ticket> pages = ticketRepository.findAll(pageable);
        List<Triple<String, String, String>> resolvedFilters = new ArrayList<>();
        if (getAllTicketsRequest.getFilterParams() != null && !getAllTicketsRequest.getFilterParams().isEmpty())
            getAllTicketsRequest.getFilterParams().forEach(f -> resolvedFilters.add(resolveFilter(f)));

        ticketsDto.getTickets().addAll(pages.getContent().stream()
                .filter(t -> filter(t, resolvedFilters))
                .map(ticketMapper::toDto)
                .toList());
        return ticketsDto;
    }

    @Transactional
    public void deleteWithVenue(VenueDto venueDto) {
        List<Ticket> tickets = ticketRepository.findAll();
        Venue venue = venueMapper.toEntity(venueDto);
        List<Ticket> filteredTickets = tickets.stream()
                .filter(t -> Objects.equals(t.getVenue(), venue))
                .toList();
        ticketRepository.deleteAll(filteredTickets);
    }

    public Integer getWithVenue(String name, Integer capacity, VenueType type) {
        Venue specifiedVenue = Venue.builder().name(name).capacity(capacity).type(type).build();
        List<Ticket> tickets = ticketRepository.findAll();
        long amount = tickets.stream().filter(ticket -> {
            Venue venue = ticket.getVenue();
            return Objects.equals(venue, specifiedVenue);
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
        if (Arrays.stream(FilterOperations.values()).filter(it -> Objects.equals(it.getValue(), split[1])).toList().isEmpty()) {
            throw new FindAllException(String.format("Filter operation doesnt correct : %s", split[1]));
        }
        return Triple.of(split[0], split[1], split[2]);
    }

    private Sort resolveSort(List<String> sort) {
        List<Sort.Order> orders = new ArrayList<>();
        sort.forEach(it -> {
            String[] split = it.split("_");
            if (split.length != 2) {
                throw new IncorrectSortException(String.format("The sorting must consist of exactly 2 arguments : %s", sort));
            }
            if (!Objects.equals(split[1], "asc") && !Objects.equals(split[1], "desc")) {
                throw new FindAllException("Sort second parameter must be asc or desc");
            }
            if (split[1].equalsIgnoreCase("asc")) {
                orders.add(Sort.Order.asc(split[0]));
            } else {
                orders.add(Sort.Order.desc(split[0]));
            }
        });
        return Sort.by(orders);
    }

    private boolean filter(Ticket ticket, List<Triple<String, String, String>> resolvedFilters) {
        AtomicBoolean filterResult = new AtomicBoolean(true);
        resolvedFilters.forEach(resolvedFilter -> {
            String field = resolvedFilter.getLeft().toLowerCase();
            String operation = resolvedFilter.getMiddle();
            String value = resolvedFilter.getRight();

            switch (field) {
                case "name" ->
                        filterResult.set(filterResult.get() && strOperationFilter(ticket.getName(), operation, value));
                case "price" ->
                        filterResult.set(filterResult.get() && numOperationFilter(ticket.getPrice(), operation, Integer.valueOf(value)));
                case "type" ->
                        filterResult.set(filterResult.get() && strOperationFilter(ticket.getType().name(), operation, value));

            }
        });
        return filterResult.get();
    }

    private boolean strOperationFilter(String argument, String operation, String value) {
        return switch (operation) {
            case "eq" -> argument.equals(value);
            case "neq" -> !argument.equals(value);
            case "more" -> value.length() < argument.length();
            case "less" -> value.length() > argument.length();
            default -> throw new IllegalStateException("Unexpected value: " + operation);
        };
    }

    private boolean numOperationFilter(Number argument, String operation, Number value) {
        return switch (operation) {
            case "eq" -> Objects.equals(argument, value);
            case "neq" -> !Objects.equals(argument, value);
            case "more" -> argument.intValue() > value.intValue();
            case "less" -> argument.intValue() < value.intValue();
            default -> throw new IllegalStateException("Unexpected value: " + operation);
        };
    }

}
