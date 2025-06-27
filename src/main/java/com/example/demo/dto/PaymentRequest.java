package com.example.demo.dto;

import com.example.demo.entity.Payment;

public class PaymentRequest {
    private Integer orderId;
    private Payment.PaymentMethod paymentMethod;
    private String email;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Payment.PaymentMethod getPaymentmethod() {
        return paymentMethod;
    }

    public void setPaymentmethod(Payment.PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
