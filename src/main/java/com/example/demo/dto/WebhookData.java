package com.example.demo.dto;

/**
 * WebhookData 是用來接收第三方金流回傳資料的 DTO。
 * 通常來自綠界或其他金流平台的通知，用於更新付款狀態與寄送信件。
 */
public class WebhookData {

    private Integer orderId;
    private String email;

    // 🟢 無參數建構子（必要）
    public WebhookData() {
    }

    // 🟢 有參數建構子（可有可無）
    public WebhookData(Integer orderId, String email) {
        this.orderId = orderId;
        this.email = email;
    }

    // 🟢 Getter & Setter
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // 🟢 toString（除錯用，可有可無）
    @Override
    public String toString() {
        return "WebhookData{" +
                "orderId=" + orderId +
                ", email='" + email + '\'' +
                '}';
    }
}
