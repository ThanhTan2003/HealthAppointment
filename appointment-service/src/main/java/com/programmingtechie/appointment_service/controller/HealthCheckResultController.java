package com.programmingtechie.appointment_service.controller;

import com.programmingtechie.appointment_service.dto.response.His.HealthCheckResultResponse;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.service.HealthCheckResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/appointment/health-check-result")
@RequiredArgsConstructor
public class HealthCheckResultController {
    final HealthCheckResultService healthCheckResultService;

    @GetMapping("/sync")
    @PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc')")
    public ResponseEntity<Void> syncHealthCheckResult() {
        return ResponseEntity.ok(healthCheckResultService.syncHealthCheckResult());
    }

    @GetMapping("/public/sync/from-his")
    public ResponseEntity<Void> syncHealthCheckResultFromHis(
            @RequestParam("expiryDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiryDateTime,
            @RequestParam("hmac") String hmac) {
        return ResponseEntity.ok(healthCheckResultService.syncHealthCheckResultFromHIS(expiryDateTime, hmac));
    }
}
