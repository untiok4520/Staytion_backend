package com.example.demo.controller;

import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.WebhookData;
import com.example.demo.dto.EcpayRequest;
import com.example.demo.entity.Payment;
import com.example.demo.service.PaymentService;
import com.example.demo.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PaymentRepository paymentRepository;

    // ✅ 建立付款請求（非綠界測試流程）
    @PostMapping("/process")
    public ResponseEntity<?> processPayment(@RequestBody PaymentRequest request) {
        String paymentUrl = paymentService.processPayment(request);
        return ResponseEntity.ok(Map.of("paymentUrl", paymentUrl));
    }

    // ✅ 處理金流 webhook 回傳（綠界付款成功會呼叫這裡）
    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody WebhookData data) {
        paymentService.handleWebhook(data);
        return ResponseEntity.ok("✅ Webhook received");
    }

    // ✅ 查詢所有付款
    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentRepository.findAll();
    }

    // ✅ 查詢單筆付款
    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPaymentById(@PathVariable Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);
        return payment.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ✅ 修改付款資訊
    @PutMapping("/{id}")
    public ResponseEntity<Payment> updatePayment(@PathVariable Long id, @RequestBody Payment updated) {
        return paymentRepository.findById(id).map(p -> {
            p.setStatus(updated.getStatus());
            p.setMethod(updated.getMethod());
            return ResponseEntity.ok(paymentRepository.save(p));
        }).orElse(ResponseEntity.notFound().build());
    }

    // ✅ 刪除付款紀錄
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        if (paymentRepository.existsById(id)) {
            paymentRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ✅ 🔥 新增綠界付款 API（產生表單並自動跳轉）
    @PostMapping("/ecpay")
    public ResponseEntity<String> ecpay(@RequestBody EcpayRequest request) {
        String htmlForm = paymentService.generateEcpayForm(request);
        return ResponseEntity.ok()
                .header("Content-Type", "text/html")
                .body(htmlForm);
    }
}
