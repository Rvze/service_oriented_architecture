package com.example.spring_service.endpoint;

import com.example.spring_service.dto.AmountResponse;
import com.example.spring_service.dto.DeleteVenueRequest;
import com.example.spring_service.dto.GetUniquePricesResponse;
import com.example.spring_service.dto.GetWithVenueRequest;
import com.example.spring_service.service.TicketService;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import java.util.List;

@Endpoint
public class OperationsEndpoint {
    private static final String NAMESPACE_URI = "com/example/spring_service/dto";
    private final TicketService ticketService;

    public OperationsEndpoint(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteVenueRequest")
    public void deleteWithVenue(@RequestPayload DeleteVenueRequest venueDto) {
        ticketService.deleteWithVenue(venueDto.getVenueDto());
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getWithVenueRequest")
    @ResponsePayload
    public AmountResponse getWithVenue(@RequestPayload GetWithVenueRequest venueRequest) {
        Integer amount = ticketService.getWithVenue(venueRequest.getName(), venueRequest.getCapacity(), venueRequest.getVenueType());
        AmountResponse amountResponse = new AmountResponse();
        amountResponse.setAmount(amount);
        return amountResponse;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUniquePricesResponse")
    @ResponsePayload
    public GetUniquePricesResponse getUniquePrices() {
        List<Integer> uniquePrices = ticketService.getUniquePrices();
        GetUniquePricesResponse uniquePricesResponse = new GetUniquePricesResponse();
        uniquePricesResponse.getPrice().addAll(uniquePrices);
        return uniquePricesResponse;
    }

}
