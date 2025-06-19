package com.example.demo.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUserId(Long userId);

	@Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems i WHERE i.roomType.hotel.id = :hotelId")
	List<Order> findByRoomTypeHotelId(@Param("hotelId") Long hotelId);

//  狀態日期篩選
	@Query("""
			    SELECT o FROM Order o
			    WHERE (:status IS NULL OR o.status = :status)
			      AND (:start IS NULL OR o.createdAt >= :start)
			      AND (:end IS NULL OR o.createdAt <= :end)
			""")
	Page<Order> searchByStatusAndDateRange(@Param("status") String status, @Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end, Pageable pageable);

//  月報表
	@Query("""
			    SELECT NEW map(FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m') as month, SUM(o.totalPrice) as totalRevenue)
			    FROM Order o
			    WHERE FUNCTION('YEAR', o.createdAt) = :year
			    GROUP BY FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m')
			    ORDER BY FUNCTION('DATE_FORMAT', o.createdAt, '%Y-%m')
			""")
	List<Map<String, Object>> getMonthlyRevenue(@Param("year") int year);

//  訂單趨勢
	@Query("""
			    SELECT NEW map(o.createdAt as date, COUNT(o.id) as orderCount)
			    FROM Order o
			    WHERE o.createdAt BETWEEN :start AND :end
			    GROUP BY o.createdAt
			    ORDER BY o.createdAt
			""")
	List<Map<String, Object>> getOrderTrend(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}
