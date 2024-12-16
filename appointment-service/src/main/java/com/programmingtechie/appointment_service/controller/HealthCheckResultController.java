package com.programmingtechie.appointment_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.appointment_service.service.HealthCheckResultService;

import lombok.RequiredArgsConstructor;

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
}
