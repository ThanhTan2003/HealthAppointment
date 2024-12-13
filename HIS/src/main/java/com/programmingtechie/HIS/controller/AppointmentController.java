package com.programmingtechie.HIS.controller;

import com.programmingtechie.HIS.dto.request.HealthCheckResultRequest;
import com.programmingtechie.HIS.dto.response.AppointmentResponse;
import com.programmingtechie.HIS.dto.response.PageResponse;
import com.programmingtechie.HIS.model.Appointment;
import com.programmingtechie.HIS.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {

        // Tạo body của response
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false)); // đường dẫn của request

        // Trả về response với mã 500
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/his/sync/appointments")
    public ResponseEntity<Void> syncAppointmentsFromAppointmentSystem() {
        // Logic để HIS gọi hệ thống Đặt Lịch lấy dữ liệu
        appointmentService.syncAppointmentsFromAppointmentSystem();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/create/health-check-result")
    public void createHealthCheckResult(HealthCheckResultRequest request)
    {
        appointmentService.createHealthCheckResult(request);
    }
    @GetMapping("/get-all")
    public PageResponse<AppointmentResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return appointmentService.getAll(page, size);
    }
}
