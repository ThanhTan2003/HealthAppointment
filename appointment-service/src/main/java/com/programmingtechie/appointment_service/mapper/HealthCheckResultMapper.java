package com.programmingtechie.appointment_service.mapper;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.response.His.HealthCheckResultResponse;
import com.programmingtechie.appointment_service.model.HealthCheckResult;
import com.programmingtechie.appointment_service.repository.httpClient.HisClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class HealthCheckResultMapper {
    final HisClient hisClient;

    @Value("${his.secretKey}")
    private String hisSecretKey;

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

    public HealthCheckResultResponse toHealthCheckResultResponse(HealthCheckResult healthCheckResult) {
        LocalDateTime expiryDateTime = LocalDateTime.now().plusMinutes(10);
        List<String> params = new ArrayList<>();
        params.add(healthCheckResult.getFileName());
        params.add(healthCheckResult.getBucketName());
        params.add(expiryDateTime.toString());

        String message = createMessage(params);

        String Hmac = "";
        try {
            Hmac = generateHmac(message, hisSecretKey);
        } catch (Exception e) {
            log.error("Error generating HMAC", e);
        }
        String URL = null;
        try {
            URL = hisClient.generateFileUrlPublic(
                    healthCheckResult.getFileName(), healthCheckResult.getBucketName(), expiryDateTime, Hmac);
        } catch (Exception e) {
            log.error(e.getMessage().toString());
            // throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
        }
        return HealthCheckResultResponse.builder()
                .id(healthCheckResult.getId())
                .name(healthCheckResult.getName())
                .URL(URL)
                .build();
    }
}
