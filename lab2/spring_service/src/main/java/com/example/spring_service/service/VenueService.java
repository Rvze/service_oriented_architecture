package com.example.spring_service.service;

import com.example.spring_service.dto.VenueDto;
import com.example.spring_service.mapper.VenueMapper;
import com.example.spring_service.model.Venue;
import com.example.spring_service.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

}
