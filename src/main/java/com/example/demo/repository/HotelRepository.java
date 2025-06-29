package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.entity.Hotel;
import com.example.demo.projection.HotelProjection;

import java.util.Optional;

public interface HotelRepository extends JpaRepository<Hotel, Long>{
	List<Hotel> findByOwnerId(Long ownerId);
    List<Hotel> findByHnameContaining(String keyword);
    Optional<Hotel> findByHname(String hname);

	@Query(value = """
			SELECT
			    h.id AS hotelId,
			    h.hname AS hotelName,
			    c.cname AS cityName,
			    d.dname AS districtName,
			    COUNT(DISTINCT r.user_id) AS reviewCount,
			    MIN(ro.price) AS lowestPrice,
			    ROUND(AVG(r.score), 1) AS averageRating,
			    i.img_url AS coverImageUrl
			FROM hotels h
			JOIN districts d ON h.district_id = d.id
			JOIN cities c ON c.id = d.city_id
			LEFT JOIN images i ON i.hotel_id = h.id AND i.is_cover = 1
			LEFT JOIN reviews r ON r.hotel_id = h.id
			LEFT JOIN room_types ro ON ro.hotel_id = h.id
			GROUP BY h.id, h.hname, c.cname, d.dname, i.img_url
			ORDER BY averageRating DESC
			LIMIT 10
			      
			""", nativeQuery = true)

	List<HotelProjection> findTopHotels();



}
