package com.example.demo.controller;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.WebhookData;
import com.example.demo.entity.Payment;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailService emailService;

    // ✅ 處理付款建立
    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest request) {
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setMethod(request.getMethod());
        payment.setStatus(PaymentStatus.UNPAID); // 預設未付款
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // 模擬金流連結（之後可換綠界）
        String fakePaymentUrl = "https://fakepay.com/pay?orderId=" + request.getOrderId();

        return ResponseEntity.ok(Map.of("paymentUrl", fakePaymentUrl));
    }

    // ✅ 模擬綠界付款完成 + 自動寄信
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody WebhookData data) {
        Payment payment = paymentRepository.findByOrderId(data.getOrderId());
        if (payment != null) {
            payment.setStatus(PaymentStatus.PAID);
            paymentRepository.save(payment);

            // 寄出訂單確認信
            emailService.sendOrderConfirmation(data.getEmail(), data.getOrderId());
        }
        return ResponseEntity.ok("✅ Webhook received");
    }
}
