package com.example.demo.controller;

import com.example.demo.entity.OrderItem;
import com.example.demo.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
@CrossOrigin
public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    // 建立一筆訂單明細
    @PostMapping
    public OrderItem createOrderItem(@RequestBody OrderItem item) {
        return orderItemRepository.save(item);
    }

    // 查詢某張訂單的所有明細
    @GetMapping("/order/{orderId}")
    public List<OrderItem> getItemsByOrderId(@PathVariable Integer orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }
}
