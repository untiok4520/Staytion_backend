package com.example.demo.repository;

import com.example.demo.entity.Favorite;
import com.example.demo.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    void deleteByUserIdAndHotelId(Long userId, Long hotelId);
    boolean existsByUserIdAndHotelId(Long userId, Long hotelId);

    @Query("""
        SELECT f.hotel FROM Favorite f
        WHERE f.userId = :userId
        AND (:city IS NULL OR f.hotel.district.city.cname = :city)
    """)
    List<Hotel> findFavoriteHotelsByUserIdAndCity(@Param("userId") Long userId, @Param("city") String city);




}
