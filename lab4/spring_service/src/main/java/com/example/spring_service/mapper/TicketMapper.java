package com.example.spring_service.mapper;

import com.example.spring_service.dto.TicketDto;
import com.example.spring_service.dto.TicketDtoRequest;
import com.example.spring_service.dto.TicketDtoResponse;
import com.example.spring_service.model.Ticket;
import com.example.spring_service.model.TicketType;
import org.mapstruct.*;

import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TicketMapper {
    @Mapping(source = "type", target = "type", qualifiedByName = "mapTicketType")
    @Mapping(source = "creationDate", target = "creationDate", qualifiedByName = "mapCreationDate")
    Ticket toEntity(TicketDtoRequest ticketDto);

    TicketDto toDto(Ticket ticket);

    @Mapping(target = "personId", source = "personId", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.SET_TO_NULL)
    @Mapping(target = "id", ignore = true)
    void update(@MappingTarget Ticket ticket, TicketDto ticketDto);

    @Named("mapTicketType")
    default TicketType mapTicketType(String type) {
        try {
            return TicketType.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(String.format("The passed ticket type is not valid - %s", type));
        }
    }

    @Named("mapCreationDate")
    default LocalDate mapCreationDate(XMLGregorianCalendar creationDate) {
        return creationDate == null ? LocalDate.now() : creationDate.toGregorianCalendar().toZonedDateTime().toLocalDate();
    }
}
