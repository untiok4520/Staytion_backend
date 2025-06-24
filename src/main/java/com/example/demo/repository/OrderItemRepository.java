package com.example.demo.repository;

import com.example.demo.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    @Query("""
    SELECT DISTINCT ri.roomType.hotel.hname 
      FROM OrderItem ri 
     WHERE ri.order.id = :orderId
  """)
    List<String> findDistinctHotelNamesByOrderId(@Param("orderId") Long orderId);

    // ✅ 查詢某一筆訂單的所有明細（推薦命名）
    List<OrderItem> findAllByOrderId(Integer orderId);

}