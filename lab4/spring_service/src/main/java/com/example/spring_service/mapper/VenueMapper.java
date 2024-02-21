package com.example.spring_service.mapper;

import com.example.spring_service.dto.VenueDto;
import com.example.spring_service.model.Venue;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface VenueMapper {
    Venue toEntity(VenueDto venueDto);

    VenueDto toDto(Venue venue);
}
