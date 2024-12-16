package com.programmingtechie.HIS.controller;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.HIS.dto.request.HealthCheckResultRequest;
import com.programmingtechie.HIS.dto.response.HealthCheckResultResponse;
import com.programmingtechie.HIS.dto.response.HealthCheckResultsDeletedResponse;
import com.programmingtechie.HIS.dto.response.PageResponse;
import com.programmingtechie.HIS.service.HealthCheckResultService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/his/health-check-result")
@RequiredArgsConstructor
public class HealthCheckResultController {

    private final HealthCheckResultService healthCheckResultService;

    @PostMapping("/create")
    public void createHealthCheckResult(HealthCheckResultRequest request) {
        healthCheckResultService.createHealthCheckResult(request);
    }

    @GetMapping("/generate-file-url")
    public ResponseEntity<String> generateFileUrl(
            @RequestParam("fileName") String fileName, @RequestParam("bucketName") String bucketName) {
        return ResponseEntity.ok(healthCheckResultService.generateFileUrl(fileName, bucketName));
    }

    @GetMapping("/public/generate-file-url")
    public ResponseEntity<String> generateFileUrlPublic(
            @RequestParam("fileName") String fileName,
            @RequestParam("bucketName") String bucketName,
            @RequestParam("expiryDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime expiryDateTime,
            @RequestParam("hmac") String hmac) {

        return ResponseEntity.ok(
                healthCheckResultService.generateFileUrlPublic(fileName, bucketName, expiryDateTime, hmac));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteHealthCheckResult(@PathVariable String id) {
        return ResponseEntity.ok(healthCheckResultService.deleteHealthCheckResult(id));
    }

    @DeleteMapping("/delete-file")
    public ResponseEntity<Void> deleteFile(
            @RequestParam("fileName") String fileName, @RequestParam("bucketName") String bucketName) {
        return ResponseEntity.ok(healthCheckResultService.deleteFile(fileName, bucketName));
    }

    @GetMapping("/public/sync/from-medical-appointment-system")
    public ResponseEntity<PageResponse<HealthCheckResultResponse>>
            getHealthCheckResultResponseForMedicalAppointmentSystem(
                    @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                            LocalDateTime startDate,
                    @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                    @RequestParam("expiryDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                            LocalDateTime expiryDateTime,
                    @RequestParam("page") int page,
                    @RequestParam("size") int size,
                    @RequestParam("hmac") String hmac) {

        PageResponse<HealthCheckResultResponse> pageResponse =
                healthCheckResultService.getHealthCheckResultResponseForMedicalAppointmentSystem(
                        startDate, endDate, expiryDateTime, page, size, hmac);

        return ResponseEntity.ok(pageResponse);
    }

    @GetMapping("/get/deleted/public/sync/from-medical-appointment-system")
    public ResponseEntity<PageResponse<HealthCheckResultsDeletedResponse>>
            getHealthCheckResultsDeletedForMedicalAppointmentSystem(
                    @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
                    @RequestParam("expiryDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                            LocalDateTime expiryDateTime,
                    @RequestParam("page") int page,
                    @RequestParam("size") int size,
                    @RequestParam("hmac") String hmac) {

        return ResponseEntity.ok(healthCheckResultService.getHealthCheckResultsDeletedForMedicalAppointmentSystem(
                endDate, expiryDateTime, page, size, hmac));
    }

    @GetMapping("/deleted/public/sync/from-medical-appointment-system")
    public ResponseEntity<Void> deletedHealthCheckResultsDeletedForMedicalAppointmentSystem(
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam("expiryDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime expiryDateTime,
            @RequestParam("hmac") String hmac) {

        return ResponseEntity.ok(healthCheckResultService.deletedHealthCheckResultsDeletedForMedicalAppointmentSystem(
                endDate, expiryDateTime, hmac));
    }
}
