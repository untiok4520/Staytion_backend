package com.example.demo.repository;

import com.example.demo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, JpaSpecificationExecutor<Review> {
    List<Review> findByHotelId(Long hotelId);
    List<Review> findByUserId(Long userId);
    boolean existsByOrderId(Long orderId);
    Optional<Review> findByOrderId(Long orderId);


}
