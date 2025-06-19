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
import com.example.demo.entity.Payment;

public interface OrderRepository extends JpaRepository<Order, Long> {
	List<Order> findByUserId(Long userId);

	@Query("SELECT DISTINCT o FROM Order o JOIN o.orderItems i WHERE i.roomType.hotel.id = :hotelId")
	List<Order> findByRoomTypeHotelId(@Param("hotelId") Long hotelId);

//  狀態日期關鍵字篩選
	@Query("""
			    SELECT DISTINCT o FROM Order o
			    JOIN o.orderItems i
			    JOIN i.roomType rt
			    JOIN rt.hotel h
			    JOIN o.payment p
			    WHERE (
			        o.user.id = :userId OR
			        h.owner.id = :userId
			    )
			    AND (:status IS NULL OR o.status = :status)
			    AND (:start IS NULL OR o.createdAt >= :start)
			    AND (:end IS NULL OR o.createdAt <= :end)
			     AND (:paymentMethod IS NULL OR p.method = :paymentMethod)
			    AND (
			        :keyword IS NULL OR
			        LOWER(h.hname) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			        LOWER(rt.rname) LIKE LOWER(CONCAT('%', :keyword, '%'))
			    )
			""")
	Page<Order> searchAccessibleOrdersWithKeyword(@Param("userId") Long userId,
			@Param("status") Order.OrderStatus status, @Param("start") LocalDateTime start,
			@Param("end") LocalDateTime end, @Param("keyword") String keyword,
			@Param("paymentMethod") Payment.PaymentMethod paymentMethod, Pageable pageable);

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

//	是否已經訂過「同一房型」在「某個入住期間內」的訂單
	@Query("""
			    SELECT COUNT(o) > 0
			    FROM Order o
			    JOIN o.orderItems i
			    WHERE o.user.id = :userId
			      AND i.roomType.id = :roomTypeId
			      AND o.checkInDate < :checkOut
			      AND o.checkOutDate > :checkIn
			      AND o.status = com.example.demo.entity.Order$OrderStatus.CONFIRMED
			""")
	boolean existsOverlappingOrder(@Param("userId") Long userId, @Param("roomTypeId") Long roomTypeId,
			@Param("checkIn") LocalDate checkIn, @Param("checkOut") LocalDate checkOut);

}
