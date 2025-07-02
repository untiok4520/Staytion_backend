package com.example.demo.controller;

import com.example.demo.dto.EcpayRequest;
import com.example.demo.dto.WebhookData;
import com.example.demo.dto.request.ConfirmOrderRequest;
import com.example.demo.entity.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private OrderRepository orderRepository;

    // ✅ [1] 綠界付款表單（帶 orderId）
    @PostMapping("/ecpay/{orderId}")
    public ResponseEntity<String> ecpay(@PathVariable Long orderId, @RequestBody EcpayRequest request) {
        String htmlForm = paymentService.generateEcpayForm(request, orderId);
        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(htmlForm);
    }

    // ✅ [2] 綠界 webhook 回傳處理
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody WebhookData data) {
        paymentService.handleEcpayWebhook(data);
        return ResponseEntity.ok("✅ Webhook received");
    }

    // ✅ [3] 一般付款處理（不帶 orderId）
    @PostMapping("/process")
    public ResponseEntity<String> processEcpay(@RequestBody EcpayRequest request) {
        String htmlForm = paymentService.generateEcpayForm(request);
        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(htmlForm);
    }

    // ✅ [4] 點擊「前往最後一步」時確認訂單狀態
    @PostMapping("/confirm-order")
    public ResponseEntity<String> confirmOrder(@RequestBody ConfirmOrderRequest request) {
        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("找不到訂單"));

        // 使用 Order 類別中的內部 enum：Order.OrderStatus
        order.setStatus(Order.OrderStatus.CONFIRMED);

        orderRepository.save(order);
        return ResponseEntity.ok("已確認訂單");
    }
}
