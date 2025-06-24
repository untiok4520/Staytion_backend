package com.example.demo.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {

    public void sendOrderConfirmation(String toEmail, Integer orderId) {
        // 模擬寄信（未接 SMTP）
        System.out.println("📧 已發送訂單確認信至：" + toEmail + "，訂單編號：" + orderId);
    }
}
//package com.example.demo.service;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.stereotype.Service;
//
//import jakarta.mail.MessagingException;
//import jakarta.mail.internet.MimeMessage;
//
//@Service
//public class EmailService {
//
//    @Autowired
//    private JavaMailSender mailSender;
//
//    public void sendOrderConfirmation(String toEmail, String subject, String contentHtml) {
//        try {
//            MimeMessage message = mailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
//            helper.setTo(toEmail);
//            helper.setSubject(subject);
//            helper.setText(contentHtml, true); // HTML content
//            helper.setFrom("no-reply@staytion.com");
//            mailSender.send(message);
//        } catch (MessagingException e) {
//            throw new RuntimeException("Failed to send email", e);
//        }
//    }
//}
//
