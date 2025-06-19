package com.example.demo.dto;

import com.example.demo.enums.PaymentMethod;

public class PaymentRequest {
    private Integer orderId;
    private PaymentMethod method;
    private String email;

    // getter & setter ...

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
