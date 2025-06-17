package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Image;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByHotelId(Integer hotelId);
    List<Image> findByHotelIdAndIsCover(Integer hotelId, Boolean isCover);
}
