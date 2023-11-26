package com.nmakarov.jax_rs_service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.nmakarov.jax_rs_service.dto.TicketDto;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

public class TicketClient {
    private Client client;
    private static final String URL = "http://localhost:8081/api/v1/tickets";

    public TicketClient() {
    }

    public List<TicketDto> getTickets() {
        String url = URL;
        client = ClientBuilder.newClient();
        Response response = client.target(url).request(MediaType.APPLICATION_JSON_TYPE).get();
        String json = response.readEntity(String.class);
        try {
            List<TicketDto> tickets = new ObjectMapper().registerModule(new JavaTimeModule()).readValue(json, new TypeReference<>() {
            });
            client.close();
            return tickets;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return List.of();
        }
    }

    public void deleteTicket(Long ticketId) {
        String url = URL + String.format("/%d", ticketId);
        client = ClientBuilder.newClient();
        client.target(url).request(MediaType.APPLICATION_JSON_TYPE).delete();
        client.close();
    }

    public void updateTicket(TicketDto ticketDto) {
        String url = URL + String.format("/%d", ticketDto.getId());
        client = ClientBuilder.newClient();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            String jsonBody = objectMapper.writeValueAsString(ticketDto);
            client.target(url).request(MediaType.APPLICATION_JSON_TYPE).put(Entity.entity(jsonBody, MediaType.APPLICATION_JSON_TYPE));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
