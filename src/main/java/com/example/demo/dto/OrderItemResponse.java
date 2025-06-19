package com.example.demo.dto;

import com.example.demo.entity.RoomType; // ⚠️ 別忘了加這行 import

public class OrderItemResponse {
    private Integer id;
    private Integer quantity;
    private Integer pricePerRoom;
    private Integer subtotal;
    private String paymentStatus;
    private String paymentMethod;

    // ✅ 新增欄位：roomType（包含 id、name 等）
    private RoomType roomType;

    // === Getters & Setters ===

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Integer getPricePerRoom() { return pricePerRoom; }
    public void setPricePerRoom(Integer pricePerRoom) { this.pricePerRoom = pricePerRoom; }

    public Integer getSubtotal() { return subtotal; }
    public void setSubtotal(Integer subtotal) { this.subtotal = subtotal; }

    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }

    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }

    public RoomType getRoomType() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {
        this.roomType = roomType;
    }
}
