package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.AmenityDTO;
import com.example.demo.service.AmenityService;

@RestController
@RequestMapping("/api/amenities")
@CrossOrigin(origins = "http://127.0.0.1:5500") // 根據你的前端 port
public class AmenityController {
    @Autowired
    private AmenityService amenityService;

    @GetMapping
    public List<AmenityDTO> getAllAmenities() {
        return amenityService.getAllAmenities();
    }
}