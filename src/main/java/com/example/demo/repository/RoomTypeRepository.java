package com.example.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.RoomType;

import jakarta.persistence.LockModeType;

public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
	List<RoomType> findByHotelId(Long hotelId);

	Page<RoomType> findByRnameContaining(String keyword, Pageable pageable);

//	資料庫層級鎖，避免多個交易同時搶同一房型
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT r FROM RoomType r WHERE r.id IN :ids")
	List<RoomType> findAllByIdWithLock(@Param("ids") List<Long> ids);

	
	
//	查詢房型總和
/*
	@Query("""
		    SELECT new com.example.demo.dto.response.RoomTypeSummaryDto(
		        h.id,
		        h.hname,
		        r.rname,
		        COUNT(r),
		        SUM(CASE WHEN r.isCanceled = false THEN r.quantity ELSE 0 END),
		        false,
		        AVG(r.price)
		    )
		    FROM RoomType r
		    JOIN r.hotel h
		    WHERE h.owner.id = :ownerId AND r.rname = :rname
		    GROUP BY h.id, h.hname, r.rname
		""")
		List<RoomTypeSummaryDto> summarizeByOwnerAndRoomType(@Param("ownerId") Long ownerId, @Param("rname") String rname);

	@Query("""
		    SELECT new com.example.demo.dto.response.RoomTypeSummaryDto(
		        r.rname,
		        COUNT(r),
		        SUM(CASE WHEN r.isCanceled = false THEN r.quantity ELSE 0 END),
		        AVG(r.price)
		    )
		    FROM RoomType r
		    WHERE r.hotel.owner.id = :ownerId AND r.rname = :rname
		    GROUP BY r.rname
		""")
		RoomTypeSummaryDto summarizeTotalByOwnerAndRoomType(
		    @Param("ownerId") Long ownerId,
		    @Param("rname") String rname
		);
*/
	
	
}
