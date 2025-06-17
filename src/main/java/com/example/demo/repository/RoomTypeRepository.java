package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.RoomType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
	List<RoomType> findByHotelId(Long hotelId);
	Page<RoomType> findByRnameContaining(String keyword, Pageable pageable);

}
