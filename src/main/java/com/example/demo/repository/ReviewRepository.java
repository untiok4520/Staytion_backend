package com.example.demo.repository;

import com.example.demo.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByHotelId(Long hotelId);
    List<Review> findByUserId(Long userId);
    boolean existsByOrderId(Long orderId);
    Optional<Review> findByOrderId(Long orderId);


}
