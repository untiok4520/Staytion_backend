package com.example.demo.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;
import java.util.stream.Collectors;

public class EcpayHelper {

    public static String generateCheckMacValue(Map<String, String> params, String hashKey, String hashIV) {
        try {
            // ✅ 1. 將所有參數依照 ASCII 排序（key 小到大）
            String raw = params.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));

            // ✅ 2. 加上 HashKey & HashIV
            raw = "HashKey=" + hashKey + "&" + raw + "&HashIV=" + hashIV;

            // ✅ 3. URL Encode
            raw = URLEncoder.encode(raw, "UTF-8")
                    .toLowerCase()
                    .replace("%21", "!")
                    .replace("%28", "(")
                    .replace("%29", ")")
                    .replace("%2a", "*")
                    .replace("%2d", "-")
                    .replace("%2e", ".")
                    .replace("%5f", "_");

            // ✅ 4. 轉成 SHA256 & 大寫
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(raw.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : hash) {
                sb.append(String.format("%02X", b));
            }

            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("產生 CheckMacValue 發生錯誤", e);
        }
    }
}
