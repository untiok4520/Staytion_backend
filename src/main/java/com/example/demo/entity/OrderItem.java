package com.example.demo.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // 明細主鍵

    @Column(name = "order_id")
    private Integer orderId;  // 對應主訂單 ID

    @Column(name = "user_id")
    private Integer userId;  // 使用者 ID（便於查詢使用者訂單）

    @Column(name = "hotel_id")
    private Integer hotelId;  // 飯店 ID（便於查詢飯店所有明細）

    @Column(name = "room_type_id")
    private Integer roomTypeId;

    private Integer quantity;

    @Column(name = "price_per_room")
    private BigDecimal pricePerRoom;

    private BigDecimal subtotal;

    // 🔻 Getter / Setter 🔻

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getHotelId() {
        return hotelId;
    }

    public void setHotelId(Integer hotelId) {
        this.hotelId = hotelId;
    }

    public Integer getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(Integer roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPricePerRoom() {
        return pricePerRoom;
    }

    public void setPricePerRoom(BigDecimal pricePerRoom) {
        this.pricePerRoom = pricePerRoom;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}