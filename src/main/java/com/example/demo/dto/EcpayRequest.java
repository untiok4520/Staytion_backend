package com.example.demo.dto;

import com.example.demo.entity.Payment;
import com.fasterxml.jackson.annotation.JsonProperty;

public class EcpayRequest {

    private Long userId;

    @JsonProperty("paymentMethod") // 對應前端欄位名稱
    private Payment.PaymentMethod paymentMethod;

    private String email;
    private String phone;
    private String roomTypeName;
    // Getter & Setter
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Payment.PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Payment.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    // ✅ 🔽 Getter & Setter for roomTypeName
    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }
}
