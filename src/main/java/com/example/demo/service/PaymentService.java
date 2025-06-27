package com.example.demo.service;

import com.example.demo.dto.EcpayRequest;
import com.example.demo.dto.WebhookData;
import com.example.demo.entity.Order;
import com.example.demo.entity.Payment;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.utils.EcpayHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderRepository orderRepository;

    private static final DateTimeFormatter ECPAY_DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    // ✅ [1] 用 orderId 產生綠界表單（個別指定用）
    public String generateEcpayForm(EcpayRequest request, Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("找不到訂單"));

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(request.getPaymentMethod());
        payment.setStatus(Payment.PaymentStatus.UNPAID);
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", "2000132");
        params.put("MerchantTradeNo", "EC" + System.currentTimeMillis());
        params.put("MerchantTradeDate", LocalDateTime.now().minusSeconds(5).format(ECPAY_DATE_FORMATTER)); // ✅ 修正格式
        params.put("PaymentType", "aio");
        params.put("TotalAmount", String.valueOf(order.getTotalPrice().intValue()));
        params.put("TradeDesc", "訂房付款");
        params.put("ItemName", "訂房金額");
        params.put("ReturnURL", "http://localhost:8080/api/payments/webhook");
        params.put("ClientBackURL", "http://127.0.0.1:5500/pages/booking_success.html");
        params.put("ChoosePayment", "ALL");
        params.put("EncryptType", "1");

        params.put("CustomField1", String.valueOf(orderId));

        String hashKey = "5294y06JbISpM5x9";
        String hashIV = "v77hoKGq4kWxNNIS";
        String checkMacValue = EcpayHelper.generateCheckMacValue(params, hashKey, hashIV);
        params.put("CheckMacValue", checkMacValue);

        StringBuilder sb = new StringBuilder();
        sb.append("<form id='ecpay-form' method='post' action='https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5'>");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(String.format("<input type='hidden' name='%s' value='%s'/>", entry.getKey(), entry.getValue()));
        }
        sb.append("</form>");
        sb.append("<script>document.getElementById('ecpay-form').submit();</script>");
        return sb.toString();
    }

    // ✅ [2] 用 userId 自動找最新 UNPAID 訂單，產生綠界表單（loading 頁用）
    public String generateEcpayForm(EcpayRequest request) {
        List<Order> orders = orderRepository.findByUserId(request.getUserId());

        Order targetOrder = orders.stream()
                .filter(o -> o.getPayment() != null &&
                        o.getPayment().getStatus() == Payment.PaymentStatus.UNPAID)
                .max((o1, o2) -> o1.getCreatedAt().compareTo(o2.getCreatedAt()))
                .orElseThrow(() -> new RuntimeException("找不到使用者的未付款訂單"));

        Payment payment = targetOrder.getPayment();

        Map<String, String> params = new HashMap<>();
        params.put("MerchantID", "2000132");
        params.put("MerchantTradeNo", "EC" + System.currentTimeMillis());
        params.put("MerchantTradeDate", LocalDateTime.now().minusSeconds(5).format(ECPAY_DATE_FORMATTER)); // ✅ 修正格式
        params.put("PaymentType", "aio");
        params.put("TotalAmount", String.valueOf(targetOrder.getTotalPrice().intValue()));
        params.put("TradeDesc", "訂房付款");
        params.put("ItemName", "訂房金額");
        params.put("ReturnURL", "http://localhost:8080/api/payments/webhook");
        params.put("ClientBackURL", "http://127.0.0.1:5500/pages/booking_success.html");
        params.put("ChoosePayment", "ALL");
        params.put("EncryptType", "1");

        params.put("CustomField1", String.valueOf(targetOrder.getId()));

        String hashKey = "5294y06JbISpM5x9";
        String hashIV = "v77hoKGq4kWxNNIS";
        String checkMacValue = EcpayHelper.generateCheckMacValue(params, hashKey, hashIV);
        params.put("CheckMacValue", checkMacValue);

        StringBuilder sb = new StringBuilder();
        sb.append("<form id='ecpay-form' method='post' action='https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5'>");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(String.format("<input type='hidden' name='%s' value='%s'/>", entry.getKey(), entry.getValue()));
        }
        sb.append("</form>");
        sb.append("<script>document.getElementById('ecpay-form').submit();</script>");
        return sb.toString();
    }

    // ✅ [3] 處理綠界 webhook，更新付款狀態
    public void handleEcpayWebhook(WebhookData data) {
        Payment payment = paymentRepository.findByOrderId(data.getOrderId());
        if (payment == null) {
            throw new RuntimeException("找不到對應付款資料");
        }

        payment.setStatus(Payment.PaymentStatus.PAID);
        paymentRepository.save(payment);

        System.out.println("✅ 付款成功，已更新訂單狀態");
    }
}
