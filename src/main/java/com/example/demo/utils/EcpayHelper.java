package com.example.demo.utils;

import java.security.MessageDigest;
import java.util.Map;
import java.util.stream.Collectors;

public class EcpayHelper {

    // ✅ 產生綠界 CheckMacValue 檢查碼
    public static String generateCheckMacValue(Map<String, String> params, String hashKey, String hashIV) {
        try {
            // 1. 將所有參數依照 ASCII 排序（key 小到大）
            String raw = params.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .map(e -> e.getKey() + "=" + e.getValue())
                    .collect(Collectors.joining("&"));

            // 2. 加上 HashKey & HashIV（✅ 注意：使用原始字串，不做 encode）
            raw = "HashKey=" + hashKey + "&" + raw + "&HashIV=" + hashIV;

            // 3. 將原始字串轉為 SHA-256 並轉為大寫十六進位
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
