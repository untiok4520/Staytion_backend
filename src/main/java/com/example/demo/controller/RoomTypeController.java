package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.RoomTypeDTO;
import com.example.demo.service.RoomTypeService;

@RestController
@RequestMapping("/api/hotels")
public class RoomTypeController {

    @Autowired
    private RoomTypeService roomTypeService;

    @GetMapping("/{hotelId}/room_types")
    public List<RoomTypeDTO> getRoomTypesByHotel(@PathVariable Long hotelId) {
        return roomTypeService.findRoomTypesByHotel(hotelId);
    }

    // 保留全部查詢方法
    @GetMapping("/room_types")
    public List<RoomTypeDTO> getAllRoomTypes() {
        return roomTypeService.getAllRoomTypeDtos();
    }

    // 這段可選
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