package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("""
    SELECT DISTINCT ri.roomType.hotel.hname 
      FROM OrderItem ri 
     WHERE ri.order.id = :orderId
  """)
    List<String> findDistinctHotelNamesByOrderId(@Param("orderId") Long orderId);

    // ✅ 查詢某一筆訂單的所有明細（推薦命名）
    List<OrderItem> findAllByOrderId(Integer orderId);


    // 更新訂單
    @Query("""
    	    SELECT oi FROM OrderItem oi
    	    JOIN oi.order o
    	    WHERE oi.roomType.id = :roomTypeId
    	      AND o.status = 'CONFIRMED'
    	      AND o.id <> :excludeOrderId
    	      AND o.checkOutDate > :checkIn
    	      AND o.checkInDate < :checkOut
    	""")
    	List<OrderItem> findOverlappingItems(
    	    @Param("roomTypeId") Long roomTypeId,
    	    @Param("checkIn") LocalDate checkIn,
    	    @Param("checkOut") LocalDate checkOut,
    	    @Param("excludeOrderId") Long excludeOrderId
    	);


}