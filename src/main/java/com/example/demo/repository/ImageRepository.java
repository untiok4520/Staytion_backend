package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Image;
import com.example.demo.entity.Hotel;


public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByHotelId(Long hotelId);

    List<Image> findByHotelIdAndIsCover(Long hotelId, Boolean isCover);
    
    void deleteByHotel(Hotel hotel);
}