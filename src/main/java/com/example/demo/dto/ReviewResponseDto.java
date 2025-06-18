package com.example.demo.dto;

import java.time.LocalDateTime;

public class ReviewResponseDto {
    private Long orderId;
    private Integer score;
    private String comment;
    private String reply;
    private String userName;
    private LocalDateTime createdAt;
    private Long userId;

    public ReviewResponseDto() {}

    public ReviewResponseDto(Long orderId, Integer score, String comment, String reply,
                             String userName, LocalDateTime createdAt, Long userId) {
        this.orderId = orderId;
        this.score = score;
        this.comment = comment;
        this.reply = reply;
        this.userName = userName;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
