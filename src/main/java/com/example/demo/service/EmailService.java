package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendOrderConfirmation(String toEmail, Integer orderId) {
        // 模擬寄信（未接 SMTP）
        System.out.println("📧 已發送訂單確認信至：" + toEmail + "，訂單編號：" + orderId);
    }
}
