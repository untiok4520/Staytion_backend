package com.example.demo.repository;

import com.example.demo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    List<Review> findByHotelId(Long hotelId);
    List<Review> findByUserId(Long userId);
    boolean existsByOrderId(Long orderId);
    Optional<Review> findByOrderId(Long orderId);

    //飯店平均分數
    @Query("SELECT AVG(r.score) FROM Review r WHERE r.hotel.id = :hotelId")
    Double findAverageScoreByHotelId(@Param("hotelId") Long hotelId);


}
