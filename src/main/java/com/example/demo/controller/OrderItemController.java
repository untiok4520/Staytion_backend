package com.example.demo.controller;

import com.example.demo.dto.OrderItemResponse;
import com.example.demo.entity.OrderItem;
import com.example.demo.entity.Payment;
import com.example.demo.repository.OrderItemRepository;
import com.example.demo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/order-items")
@CrossOrigin
public class OrderItemController {

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    // ✅ 查詢某一訂單所有明細（回傳 DTO 包含付款資訊）
    @GetMapping("/order/{orderId}")
    public List<OrderItemResponse> getItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItem> items = orderItemRepository.findAllByOrderId(orderId);
        Payment payment = paymentRepository.findByOrderId(orderId);

        return items.stream().map(item -> {
            OrderItemResponse response = new OrderItemResponse();

            response.setOrderItemId(item.getId());
            response.setQuantity(item.getQuantity());
            response.setPricePerRoom(item.getPricePerRoom());
            response.setSubtotal(item.getSubtotal());

            if (item.getRoomType() != null) {
                response.setRoomTypeId(item.getRoomType().getId());
                response.setRoomTypeName(item.getRoomType().getRname()); // ✅ 改成 getRname()
                response.setRoomImgUrl(item.getRoomType().getImgUrl());

                if (item.getRoomType().getHotel() != null) {
                    response.setHotelId(item.getRoomType().getHotel().getId());
                    response.setHotelName(item.getRoomType().getHotel().getHname()); // ✅ 改成 getHname()
                }
            }

            if (payment != null) {
                response.setPaymentStatus(payment.getStatus() != null ? payment.getStatus().name() : "未知");
                response.setPaymentMethod(payment.getMethod() != null ? payment.getMethod().name() : "未知");
            } else {
                response.setPaymentStatus("未付款");
                response.setPaymentMethod("未知");
            }

            return response;
        }).collect(Collectors.toList());
    }


    // ✅ 新增一筆訂單明細
    @PostMapping
    public OrderItem createOrderItem(@RequestBody OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }

    // ✅ 查詢單筆訂單明細
    @GetMapping("/{id}")
    public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long id) {
        return orderItemRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 修改一筆訂單明細
    @PutMapping("/{id}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable Long id, @RequestBody OrderItem updatedItem) {
        return orderItemRepository.findById(id).map(item -> {
            item.setQuantity(updatedItem.getQuantity());
            item.setPricePerRoom(updatedItem.getPricePerRoom());
            item.setSubtotal(updatedItem.getSubtotal());
            item.setRoomType(updatedItem.getRoomType());
            return ResponseEntity.ok(orderItemRepository.save(item));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ 刪除一筆訂單明細
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        if (orderItemRepository.existsById(id)) {
            orderItemRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
