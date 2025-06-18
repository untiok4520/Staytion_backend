package com.example.demo.service;

import com.example.demo.dto.RoomAvailabilityCheckResponse;
import com.example.demo.entity.RoomAvailability;
import com.example.demo.repository.RoomAvailabilityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomAvailabilityService {

    @Autowired
    private RoomAvailabilityRepository roomAvailabilityRepository;

    public Optional<RoomAvailability>
        findByRoomTypeAndDate(Long roomTypeId, LocalDate date) {
        return roomAvailabilityRepository.findByRoomType_IdAndDate(roomTypeId, date);
    }

    public List<RoomAvailabilityCheckResponse>
        findByRoomTypeAndDateBetween (Long roomTypeId, LocalDate start, LocalDate end) {
        List<RoomAvailability> entities = roomAvailabilityRepository.findByRoomType_IdAndDateBetween(roomTypeId, start, end);

        return entities.stream()
                .map(e -> new RoomAvailabilityCheckResponse(
                        e.getId(),
                        e.getRoomType().getId(),
                        e.getDate(),
                        e.getAvailableQuantity()
                ))
                .collect(java.util.stream.Collectors.toList());
    }
}
