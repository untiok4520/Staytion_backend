package com.example.demo.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Image;
import com.example.demo.repository.ImageRepository;

@RestController
@RequestMapping("/api/images")
@CrossOrigin(origins = "*")  
public class ImageController {

    private final ImageRepository repo;

    public ImageController(ImageRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Image> getImages(@RequestParam(required = false) Integer hotel_id,
                                 @RequestParam(required = false) Boolean cover) {
        if (hotel_id == null) return repo.findAll();
        if (cover != null) return repo.findByHotelIdAndIsCover(hotel_id, cover);
        return repo.findByHotelId(hotel_id);
    }
}
