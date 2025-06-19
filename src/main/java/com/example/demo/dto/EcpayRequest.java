package com.example.demo.dto;

import com.example.demo.enums.PaymentMethod;

public class EcpayRequest {

    private String roomTypeName;   // 房型名稱（例如：雙人房）
    private int nights;            // 入住晚數
    private int totalAmount;       // 總金額（int）
    private PaymentMethod method;  // 付款方式（如：Credit、ATM）

    // ✅ Getter & Setter

    public String getRoomTypeName() {
        return roomTypeName;
    }

    public void setRoomTypeName(String roomTypeName) {
        this.roomTypeName = roomTypeName;
    }

    public int getNights() {
        return nights;
    }

    public void setNights(int nights) {
        this.nights = nights;
    }

    public int getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }
}
