package com.example.demo.repository;

import com.example.demo.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    // 查詢某一筆訂單的所有明細
    List<OrderItem> findByOrderId(Integer orderId);
}
