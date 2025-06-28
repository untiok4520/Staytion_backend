package com.example.demo.repository;

import com.example.demo.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByHotelId(Long hotelId);

    List<Image> findByHotelIdAndIsCover(Long hotelId, Boolean isCover);

    //查詢飯店首圖
    @Query("""
                SELECT img.imgUrl
                FROM Image img
                WHERE img.hotel.id = :hotelId AND img.isCover = true
            """)
    List<String> findCoverImageByHotelId(Long hotelId);
}