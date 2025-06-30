package com.example.demo.controller;

import com.example.demo.entity.Order;
import com.example.demo.entity.Order.OrderStatus;
import com.example.demo.repository.OrderRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders/status")
@CrossOrigin(origins = "http://127.0.0.1:5500") // 可依你前端位置調整
public class OrderStatusController {

    @Autowired
    private OrderRepository orderRepository;

    /**
     * ✅ 使用 userId 查詢該使用者最新一筆 PENDING 訂單，並將其狀態改為 CONFIRMED
     */
    @PutMapping("/confirm-latest")
    public ResponseEntity<String> confirmLatestPendingOrder(@RequestParam Long userId) {
        Optional<Order> optionalOrder = orderRepository
                .findTopByUserIdAndStatusOrderByCreatedAtDesc(userId, OrderStatus.PENDING);

        if (optionalOrder.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("找不到使用者 ID：" + userId + " 的 PENDING 訂單");
        }

        Order order = optionalOrder.get();
        order.setStatus(OrderStatus.CONFIRMED);
        orderRepository.save(order);

        return ResponseEntity.ok("訂單 ID " + order.getId() + " 狀態已更新為 CONFIRMED");
    }
}
