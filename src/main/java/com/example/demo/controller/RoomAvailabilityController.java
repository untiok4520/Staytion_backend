package com.example.demo.controller;

import com.example.demo.dto.RoomAvailabilityCheckResponse;
import com.example.demo.entity.RoomAvailability;
import com.example.demo.service.RoomAvailabilityService;
import io.swagger.v3.oas.annotations.Operation;
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

    @Operation(
            summary = "查詢指定房型某天剩餘房間數",
            description = "依房型ID與日期查詢該日可預訂的剩餘房間數量,內容包含:可用房數"
    )
    @GetMapping("/{roomTypeId}/one-count")
    private Integer getOneCount(
            @PathVariable Long roomTypeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
            return roomAvailabilityService
                    .findByRoomTypeAndDate(roomTypeId, date)
                    .map(RoomAvailability::getAvailableQuantity)
                    .orElse(0);
    }

    @Operation(
            summary = "查詢指定房型在某時間區間的每日剩餘房間數",
            description = "依房型ID與日期區間查詢該期間內每一天的剩餘房間數量,內容包含:房型ID、日期、每日可用房數"
    )
    @GetMapping("/{roomTypeId}/availability")
    private List<RoomAvailabilityCheckResponse> getAvailableQuantityRange(
            @PathVariable Long roomTypeId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start, LocalDate end) {
        return roomAvailabilityService.findByRoomTypeAndDateBetween(roomTypeId, start, end);
        }
}
