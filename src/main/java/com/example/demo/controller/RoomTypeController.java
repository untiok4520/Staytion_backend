package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.demo.dto.RoomTypeDTO;
import com.example.demo.service.RoomTypeService;

@RestController
@RequestMapping("/api")
public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("/room-types")
    public List<RoomTypeDTO> getAllRoomTypes() {
        return roomTypeService.getAllRoomTypeDtos();
    }

    @GetMapping("/hotels/{hotelId}/room-types")
    public List<RoomTypeDTO> getRoomTypesByHotel(@PathVariable Long hotelId) {
        return roomTypeService.findRoomTypesByHotel(hotelId);
    }

    @GetMapping("/hotels/{hotelId}/room-types/filter")
    public List<RoomTypeDTO> getRoomTypesByHotelAndCapacity(
            @PathVariable Long hotelId,
            @RequestParam Integer adults,
            @RequestParam(required = false, defaultValue = "0") Integer children
    ) {
        int people = adults + children;
        return roomTypeService.findRoomTypesByHotelAndCapacity(hotelId, people);
    }


    // @GetMapping("/room_types/{id}")
    // public ResponseEntity<RoomType> getRoomTypeById(@PathVariable Long id) {
    // RoomType roomType = roomTypeService.getRoomTypeById(id);
    // if (roomType !=null) {
    // return ResponseEntity.ok(roomType);
    // } else {
    // return ResponseEntity.notFound().build();
    // }
    // }
}