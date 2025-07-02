package com.example.demo.controller;

import com.example.demo.dto.EcpayRequest;
import com.example.demo.dto.WebhookData;
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

    // ✅ [1] 使用者送出訂單後 → 從前端 POST 到這裡 → 後端產生綠界付款表單（回傳 HTML）
    @PostMapping("/ecpay/{orderId}")
    public ResponseEntity<String> ecpay(@PathVariable Long orderId, @RequestBody EcpayRequest request) {
        String htmlForm = paymentService.generateEcpayForm(request, orderId);
        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(htmlForm);
    }

    // ✅ [2] 綠界付款完成後會發 webhook 到這裡 → 後端更新付款狀態
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody WebhookData data) {
        paymentService.handleEcpayWebhook(data);
        return ResponseEntity.ok("✅ Webhook received");
    }

    // ✅ [3] 前端 loading 頁面使用 → 不含 orderId（適用於使用 localStorage）
    @PostMapping("/process")
    public ResponseEntity<String> processEcpay(@RequestBody EcpayRequest request) {
        String htmlForm = paymentService.generateEcpayForm(request);
        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(htmlForm);
    }
}

@PostMapping("/confirm-order")
public ResponseEntity<String> confirmOrder(@RequestBody ConfirmOrderRequest request) {
    Order order = orderRepository.findById(request.getOrderId())
            .orElseThrow(() -> new RuntimeException("找不到訂單"));

    order.setStatus(OrderStatus.CONFIRMED); // 狀態改成 CONFIRMED
    orderRepository.save(order);
    return ResponseEntity.ok("已確認訂單");
}
