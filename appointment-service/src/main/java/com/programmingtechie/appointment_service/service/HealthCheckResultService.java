package com.programmingtechie.appointment_service.service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.appointment_service.dto.response.His.HealthCheckResultResponse;
import com.programmingtechie.appointment_service.dto.response.His.HealthCheckResultsDeletedResponse;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.enums.Sync_History;
import com.programmingtechie.appointment_service.model.Appointment;
import com.programmingtechie.appointment_service.model.HealthCheckResult;
import com.programmingtechie.appointment_service.model.SyncHistory;
import com.programmingtechie.appointment_service.repository.AppointmentRepository;
import com.programmingtechie.appointment_service.repository.HealthCheckResultRepository;
import com.programmingtechie.appointment_service.repository.SyncHistoryRepository;
import com.programmingtechie.appointment_service.repository.httpClient.HisClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class HealthCheckResultService {
    final HealthCheckResultRepository healthCheckResultRepository;
    final SyncHistoryRepository syncHistoryRepository;
    final AppointmentRepository appointmentRepository;

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

    @Transactional
    public Void syncHealthCheckResult() {
        String appointmentValue = Sync_History.HEALTH_CHECK_RESULTS.getName();
        log.info("id: " + appointmentValue);
        SyncHistory syncHistory =
                syncHistoryRepository.findById(appointmentValue).orElse(null);
        if (syncHistory == null)
            throw new IllegalArgumentException(
                    "Đã xảy ra lỗi khi đồng bộ. Vui lòng kiểm tra và thử lại sau!");
        log.info(syncHistory.toString());

        LocalDateTime startDate = syncHistory.getDateTime();
        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime expiryDateTime = endDate.plusMinutes(15);

        Integer page = 1;
        Integer size = 50;

        List<String> params = new ArrayList<>();
        params.add(startDate.toString());
        params.add(endDate.toString());
        params.add(expiryDateTime.toString());
        params.add(page.toString());
        params.add(size.toString());

        String message = createMessage(params);

        String Hmac = "";
        log.info("----------1. Lay du lieu ve------------");
        try {
            Hmac = generateHmac(message, hisSecretKey);
        } catch (Exception e) {
            log.error("Error generating HMAC", e);
        }
        log.info("message: " + message);
        log.info("appointment.secretKey: " + hisSecretKey);
        log.info("Hmac: " + Hmac);

        PageResponse<HealthCheckResultResponse> pageResponse = null;
        try {
            pageResponse = hisClient.getHealthCheckResultResponseForMedicalAppointmentSystem(
                    startDate, endDate, expiryDateTime, page, size, Hmac);
            saveHealthCheckResultForMedicalAppointmentSystem(pageResponse.getData());
        } catch (Exception e) {
            log.error(e.getMessage().toString());
            throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
        }

        // Kiểm tra totalPages để đảm bảo không lặp lại
        int totalPages = pageResponse.getTotalPages();
        log.info("page: " + page);
        log.info("totalPages: " + totalPages);

        // Lặp qua tất cả các trang nếu có
        while (page < totalPages) {
            log.info("Fetching data for page " + page + " of " + totalPages);

            // Nếu còn trang tiếp theo
            page++;
            log.info("page: " + page);
            params.set(3, String.valueOf(page)); // Cập nhật lại page trong params
            message = createMessage(params);
            try {
                Hmac = generateHmac(message, hisSecretKey);
            } catch (Exception e) {
                log.error("Error generating HMAC for page " + page, e);
            }
            // log.info("message: " + message);
            // log.info("Hmac: " + Hmac);

            // Gửi yêu cầu tiếp theo
            try {
                pageResponse = hisClient.getHealthCheckResultResponseForMedicalAppointmentSystem(
                        startDate, endDate, expiryDateTime, page, size, Hmac);
                saveHealthCheckResultForMedicalAppointmentSystem(pageResponse.getData());
            } catch (Exception e) {
                log.error(e.getMessage().toString());
                throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
            }
            totalPages = pageResponse.getTotalPages(); // Cập nhật lại totalPages
        }
        syncHistory.setDateTime(endDate);

        log.info("----------2. deleteHealthCheckResults------------");
        deleteHealthCheckResults(endDate, expiryDateTime);

        log.info("----------3. deleteHealthCheckResultsToHis------------");
        deleteHealthCheckResultsToHis(endDate, expiryDateTime);
        syncHistoryRepository.save(syncHistory);
        return null;
    }

    public void deleteHealthCheckResults(LocalDateTime endDate, LocalDateTime expiryDateTime) {
        Integer page = 1;
        Integer size = 50;

        List<String> params = new ArrayList<>();
        params.add(endDate.toString());
        params.add(expiryDateTime.toString());
        params.add(page.toString());
        params.add(size.toString());

        String message = createMessage(params);

        String Hmac = "";
        try {
            Hmac = generateHmac(message, hisSecretKey);
        } catch (Exception e) {
            log.error("Error generating HMAC", e);
        }
        try {
            log.info("Xoa du thua ------------------");
            hisClient.getHealthCheckResultsDeletedForMedicalAppointmentSystem(
                    endDate, expiryDateTime, page, size, Hmac);
        } catch (Exception e) {
            log.error(e.getMessage().toString());
            throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
        }

        PageResponse<HealthCheckResultsDeletedResponse> pageResponse = null;
        try {
            pageResponse = hisClient.getHealthCheckResultsDeletedForMedicalAppointmentSystem(
                    endDate, expiryDateTime, page, size, Hmac);
            deleteHealthCheckResultForMedicalAppointmentSystem(pageResponse.getData());
        } catch (Exception e) {
            log.error(e.getMessage().toString());
            throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
        }

        // Kiểm tra totalPages để đảm bảo không lặp lại
        int totalPages = pageResponse.getTotalPages();
        log.info("page: " + page);
        log.info("totalPages: " + totalPages);

        // Lặp qua tất cả các trang nếu có
        while (page < totalPages) {
            log.info("Fetching data for page " + page + " of " + totalPages);

            // Nếu còn trang tiếp theo
            page++;
            log.info("page: " + page);
            params.set(3, String.valueOf(page)); // Cập nhật lại page trong params
            message = createMessage(params);
            try {
                Hmac = generateHmac(message, hisSecretKey);
            } catch (Exception e) {
                log.error("Error generating HMAC for page " + page, e);
            }
            // log.info("message: " + message);
            // log.info("Hmac: " + Hmac);

            // Gửi yêu cầu tiếp theo
            try {
                pageResponse = hisClient.getHealthCheckResultsDeletedForMedicalAppointmentSystem(
                        endDate, expiryDateTime, page, size, Hmac);
                deleteHealthCheckResultForMedicalAppointmentSystem(pageResponse.getData());
            } catch (Exception e) {
                log.error(e.getMessage().toString());
                throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
            }
            totalPages = pageResponse.getTotalPages(); // Cập nhật lại totalPages
        }
    }

    private void deleteHealthCheckResultForMedicalAppointmentSystem(List<HealthCheckResultsDeletedResponse> data) {
        for (HealthCheckResultsDeletedResponse item : data) {
            String id = item.getId();
            HealthCheckResult healthCheckResult =
                    healthCheckResultRepository.findById(id).orElse(null);
            if (healthCheckResult != null) healthCheckResultRepository.delete(healthCheckResult);
        }
    }

    public void deleteHealthCheckResultsToHis(LocalDateTime endDate, LocalDateTime expiryDateTime) {
        List<String> params = new ArrayList<>();
        params.add(endDate.toString());
        params.add(expiryDateTime.toString());

        String message = createMessage(params);

        String Hmac = "";
        try {
            Hmac = generateHmac(message, hisSecretKey);
        } catch (Exception e) {
            log.error("Error generating HMAC", e);
        }
        try {
            log.info("Xoa du thua ------------------");
            hisClient.deletedHealthCheckResultsDeletedForMedicalAppointmentSystem(endDate, expiryDateTime, Hmac);
        } catch (Exception e) {
            log.error(e.getMessage().toString());
            throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại!");
        }
    }

    private void saveHealthCheckResultForMedicalAppointmentSystem(
            List<HealthCheckResultResponse> healthCheckResultResponses) {
        for (HealthCheckResultResponse healthCheckResultResponse : healthCheckResultResponses) {

            Appointment appointment = appointmentRepository
                    .findById(healthCheckResultResponse.getAppointmentId())
                    .orElse(null);

            HealthCheckResult healthCheckResult = HealthCheckResult.builder()
                    .id(healthCheckResultResponse.getId())
                    .appointment(appointment)
                    .name(healthCheckResultResponse.getName())
                    .fileName(healthCheckResultResponse.getFileName())
                    .bucketName(healthCheckResultResponse.getBucketName())
                    .lastUpdated(healthCheckResultResponse.getLastUpdated())
                    .build();

            log.info("Saving healthCheckResult: " + healthCheckResult);

            healthCheckResultRepository.save(healthCheckResult);
        }
    }
}
