package com.example.demo.service;

import com.example.demo.dto.EcpayRequest;
import com.example.demo.dto.PaymentRequest;
import com.example.demo.dto.WebhookData;
import com.example.demo.entity.Payment;
import com.example.demo.enums.PaymentStatus;
import com.example.demo.repository.PaymentRepository;
import com.example.demo.utils.EcpayHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailService emailService;

    // ✅ 模擬付款流程
    public String processPayment(PaymentRequest request) {
        Payment payment = new Payment();
        payment.setOrderId(request.getOrderId());
        payment.setMethod(request.getMethod());
        payment.setStatus(PaymentStatus.UNPAID);
        payment.setCreatedAt(LocalDateTime.now());

        paymentRepository.save(payment);

        // 模擬付款連結
        return "https://fakepay.com/pay?orderId=" + request.getOrderId();
    }

    // ✅ 處理金流完成後的 webhook
    public void handleWebhook(WebhookData data) {
        Payment payment = paymentRepository.findByOrderId(data.getOrderId());
        if (payment != null) {
            payment.setStatus(PaymentStatus.PAID);
            paymentRepository.save(payment);

            // 發送確認信
            emailService.sendOrderConfirmation(data.getEmail(), data.getOrderId());
        }
    }

    // ✅ 產生綠界金流表單
    public String generateEcpayForm(EcpayRequest request) {
        Map<String, String> params = new LinkedHashMap<>();
        params.put("MerchantID", "2000132"); // ✅ 測試商店代號
        params.put("MerchantTradeNo", "EC" + System.currentTimeMillis());
        params.put("MerchantTradeDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));
        params.put("PaymentType", "aio");
        params.put("TotalAmount", String.valueOf(request.getTotalAmount()));
        params.put("TradeDesc", "Staytion 訂房付款");
        params.put("ItemName", request.getRoomTypeName() + " x " + request.getNights() + "晚");
        params.put("ReturnURL", "http://localhost:8080/api/payments/webhook"); // ✅ webhook 回傳位址
        params.put("ChoosePayment", "Credit"); // ✅ 付款方式
        params.put("EncryptType", "1");

        // ✅ 加密驗證用的金鑰（測試用）
        String checkMac = EcpayHelper.generateCheckMacValue(
                params, "5294y06JbISpM5x9", "v77hoKGq4kWxNNIS"
        );
        params.put("CheckMacValue", checkMac);

        // ✅ 組 HTML 表單
        StringBuilder form = new StringBuilder();
        form.append("<form id='ecpay-form' method='POST' action='https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5'>");
        for (Map.Entry<String, String> entry : params.entrySet()) {
            form.append("<input type='hidden' name='")
                    .append(entry.getKey())
                    .append("' value='")
                    .append(entry.getValue())
                    .append("'>");
        }
        form.append("</form>");
        form.append("<script>document.getElementById('ecpay-form').submit();</script>");

        return form.toString();
    }
}
