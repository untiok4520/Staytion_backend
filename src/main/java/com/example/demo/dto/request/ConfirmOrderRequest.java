package com.example.demo.dto.request;

public class ConfirmOrderRequest {

    private Long orderId;

    // 無參數建構子（必要）
    public ConfirmOrderRequest() {
    }

    // 有參數建構子（可選）
    public ConfirmOrderRequest(Long orderId) {
        this.orderId = orderId;
    }

    // Getter
    public Long getOrderId() {
        return orderId;
    }

    // Setter
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
