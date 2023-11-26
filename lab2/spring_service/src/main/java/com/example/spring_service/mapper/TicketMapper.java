package com.example.spring_service.mapper;

import com.example.spring_service.dto.TicketDto;
import com.example.spring_service.model.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TicketMapper {
    Ticket toEntity(TicketDto ticketDto);

    TicketDto toDto(Ticket ticket);

    @Mapping(target = "personId", source = "personId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    void update(@MappingTarget Ticket ticket, TicketDto ticketDto);
}
