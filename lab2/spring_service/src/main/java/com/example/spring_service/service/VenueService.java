package com.example.spring_service.service;

import com.example.spring_service.mapper.VenueMapper;
import com.example.spring_service.repository.VenueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

}
