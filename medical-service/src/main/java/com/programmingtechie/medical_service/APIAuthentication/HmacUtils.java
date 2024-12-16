package com.programmingtechie.medical_service.APIAuthentication;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

@Component
public class HmacUtils {

    public String generateHmac(String message, String secretKey) throws Exception {
        Mac sha256Hmac = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256Hmac.init(secretKeySpec);
        byte[] hashBytes = sha256Hmac.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hashBytes); // Trả về chuỗi base64 của HMAC SHA-256
    }

    public boolean verifyHmac(String message, String receivedHmac, String secretKey) throws Exception {
        String generatedHmac = generateHmac(message, secretKey);
        return generatedHmac.equals(receivedHmac); // So sánh HMAC đã gửi với HMAC đã tạo
    }

    public String createMessage(List<String> params) {
        StringBuilder messageBuilder = new StringBuilder();

        for (int i = 0; i < params.size(); i++) {
            messageBuilder.append(params.get(i));
            if (i < params.size() - 1) {
                messageBuilder.append(",");
            }
        }
        return messageBuilder.toString();
    }

    public LocalDateTime createExpiryDateTime(LocalDateTime localDateTime, Integer minute) {
        return localDateTime.plusMinutes(minute);
    }
}
