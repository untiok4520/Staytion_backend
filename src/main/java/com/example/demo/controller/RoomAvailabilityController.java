package com.example.demo.controller;

import com.example.demo.dto.RoomAvailabilityCheckResponse;
import com.example.demo.entity.RoomAvailability;
import com.example.demo.service.RoomAvailabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rooms")
public class RoomAvailabilityController {

    @Autowired
    private RoomAvailabilityService roomAvailabilityService;

    @GetMapping("/{roomTypeId}/one-count")
    private Integer getOneCount(
            @PathVariable Long roomTypeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            return roomAvailabilityService
                    .findByRoomTypeAndDate(roomTypeId, date)
                    .map(RoomAvailability::getAvailableQuantity)
                    .orElse(0);
    }

    @GetMapping("/{roomTypeId}/availability")
    private List<RoomAvailabilityCheckResponse> getAvailableQuantityRange(
            @PathVariable Long roomTypeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, LocalDate end) {
        return roomAvailabilityService.findByRoomTypeAndDateBetween(roomTypeId, start, end);
        }
}
