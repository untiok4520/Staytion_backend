package com.example.demo.service;

import com.example.demo.entity.Order;
import com.example.demo.entity.Order.OrderStatus;
import com.example.demo.repository.OrderRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderCleanupService {

    @Autowired
    private OrderRepository orderRepository;

    @Scheduled(fixedRate = 30 * 60 * 1000) // 每 30 分鐘執行一次
    @Transactional
    public void cancelExpiredOrders() {
        LocalDateTime expiredBefore = LocalDateTime.now().minusMinutes(30);

        List<Order> expiredOrders = orderRepository
                .findByStatusAndCreatedAtBefore(OrderStatus.PENDING, expiredBefore);

        for (Order order : expiredOrders) {
            order.setStatus(OrderStatus.CANCELED);
            // TODO: 可加上釋放房間庫存、清除鎖房資料等邏輯
        }

        orderRepository.saveAll(expiredOrders);

        System.out.println("✅ 已自動取消過期訂單：" + expiredOrders.size() + " 筆");
    }
}
