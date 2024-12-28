package com.programmingtechie.appointment_service.repository.httpClient;

import com.programmingtechie.appointment_service.config.AuthenticationRequestInterceptor;
import com.programmingtechie.appointment_service.dto.response.His.HealthCheckResultResponse;
import com.programmingtechie.appointment_service.dto.response.His.HealthCheckResultsDeletedResponse;
import com.programmingtechie.appointment_service.dto.response.Medical.ServiceTimeFrameInAppointmentResponse;
import com.programmingtechie.appointment_service.dto.response.Medical.ServiceTimeFrameResponse;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@FeignClient(
        name = "his-client",
        url = "${app.services.his}")
public interface HisClient {
    @GetMapping("/health-check-result/public/sync/from-medical-appointment-system")
    public PageResponse<HealthCheckResultResponse> getHealthCheckResultResponseForMedicalAppointmentSystem(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam("expiryDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiryDateTime,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("hmac") String hmac);

    @GetMapping("/health-check-result/get/deleted/public/sync/from-medical-appointment-system")
    public PageResponse<HealthCheckResultsDeletedResponse> getHealthCheckResultsDeletedForMedicalAppointmentSystem(
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam("expiryDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiryDateTime,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("hmac") String hmac);

    @GetMapping("/health-check-result/deleted/public/sync/from-medical-appointment-system")
    public void deletedHealthCheckResultsDeletedForMedicalAppointmentSystem(
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam("expiryDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiryDateTime,
            @RequestParam("hmac") String hmac);

    @GetMapping("/health-check-result/public/generate-file-url")
    public String generateFileUrlPublic(
            @RequestParam("fileName") String fileName,
            @RequestParam("bucketName") String bucketName,
            @RequestParam("expiryDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiryDateTime,
            @RequestParam("hmac") String hmac);

    @GetMapping("/appointment/sync/from-health-appointment")
    public Void syncAppointmentsFromHealthAppointment(
            @RequestParam("expiryDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiryDateTime,
            @RequestParam("hmac") String hmac);
}

