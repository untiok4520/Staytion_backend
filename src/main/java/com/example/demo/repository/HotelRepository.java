package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Hotel;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
	@Query("""
			SELECT
				h
				FROM Hotel h
			WHERE h.hname LIKE %:name%
				AND h.address LIKE :addr%
				AND h.tel LIKE %:tel%
		""")
		List<Hotel> findByNameWithAndAddrStartAndTelWith(
				@Param("name") String nameLike,
				@Param("addr") String addrStart,
				@Param("tel") String telLike);
	
	@Query("""
			SELECT h
			FROM Hotel h
			WHERE h.id = :HotelId
			""")
	Optional<Hotel> findByHotelIdWithRoomTypes(
			@Param("HotelId") String HotelId);
}
