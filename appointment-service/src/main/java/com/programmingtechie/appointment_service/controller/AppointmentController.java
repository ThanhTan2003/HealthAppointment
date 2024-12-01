package com.programmingtechie.appointment_service.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.programmingtechie.appointment_service.dto.request.AppointmentCountRequest;
import com.programmingtechie.appointment_service.dto.response.AppointmentCountResponse;
import com.programmingtechie.appointment_service.dto.response.Medical.AppointmentTimeFrameResponse;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.appointment_service.dto.request.AppointmentRequest;
import com.programmingtechie.appointment_service.dto.response.AppointmentResponse;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.service.AppointmentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
public class AppointmentController {
    final AppointmentService appointmentService;

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

    @PostMapping("/create")
    @PreAuthorize("hasRole('NguoiDung')")
    public ResponseEntity<AppointmentResponse> createAppointment(@RequestBody AppointmentRequest appointmentRequest) {
        return ResponseEntity.ok(appointmentService.createAppointment(appointmentRequest));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc')")
    public ResponseEntity<AppointmentResponse> getAppointmentById(@PathVariable String id) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(id));
    }

    @GetMapping("/patients-id/{id}")
    @PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc') or hasRole('NguoiDung')")
    public ResponseEntity<PageResponse<AppointmentResponse>> getAppointmentByPatientsId(
            @PathVariable String id,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(appointmentService.getAppointmentByPatientsId(id, page, size));
    }

    @GetMapping("/customer/patientId/{patientId}")
    @PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc') or hasRole('NguoiDung')")
    public ResponseEntity<PageResponse<AppointmentTimeFrameResponse>> getAppointmentByCustomerIdAndPatientsId(
            @PathVariable String patientId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(appointmentService.getAppointmentByCustomerIdAndPatientsId(patientId, page, size));
    }
    @GetMapping("/customer/get-all")
    @PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc') or hasRole('NguoiDung')")
    public ResponseEntity<PageResponse<AppointmentTimeFrameResponse>> getMyAppointment(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(appointmentService.getMyAppointment(page, size));
    }

    @PatchMapping("/{id}/update")
    @PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc')")
    public ResponseEntity<AppointmentResponse> updateAppointment(
            @PathVariable String id, @RequestBody Map<String, Object> updates) {
        AppointmentResponse response = appointmentService.updateAppointment(id, updates);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable String id) {
        appointmentService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<AppointmentResponse>> getAllAppointments(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(appointmentService.getAllAppointments(page, size));
    }

    @GetMapping("/get")
    @PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<AppointmentResponse>> searchAppointments(
            @RequestParam(value = "id", required = false) String id,
            @RequestParam(value = "date", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
                    LocalDate date,
            @RequestParam(value = "serviceTimeFrameId", required = false) String serviceTimeFrameId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        // Gửi yêu cầu đến service để tìm kiếm
        PageResponse<AppointmentResponse> results =
                appointmentService.searchAppointments(id, date, serviceTimeFrameId, page, size);

        return ResponseEntity.ok(results);
    }

    // API để tính tổng số lượng appointments từ một danh sách các cặp serviceTimeFrameId và date
    @PostMapping("/public/count-appointments")
    public ResponseEntity<List<AppointmentCountResponse>> countAppointments(
            @RequestBody List<AppointmentCountRequest> request) {

        List<AppointmentCountResponse> responses = appointmentService.countAppointments(request);
        return ResponseEntity.ok(responses);
    }

    // API để tính tổng số lượng appointments theo 1 serviceTimeFrameId và date
    @GetMapping("/public/count-appointments")
    public ResponseEntity<AppointmentCountResponse> countAppointmentsByParams(
            @RequestParam String serviceTimeFrameId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        AppointmentCountResponse response = appointmentService.countAppointmentsByParams(serviceTimeFrameId, date);
        return ResponseEntity.ok(response);
    }
}

//@PutMapping("/update/{id}")
//@PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc')")
//public ResponseEntity<AppointmentResponse> updateAppointment(
//        @PathVariable String id, @RequestBody AppointmentRequest appointmentRequest) {
//    return ResponseEntity.ok(appointmentService.updateAppointment(id, appointmentRequest));
//}
//
//@PutMapping("/{id}/update-status")
//@PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc')")
//public ResponseEntity<AppointmentResponse> updateAppointmentStatus(
//        @PathVariable String id, @RequestBody Map<String, String> request) {
//    String status = request.get("status");
//    AppointmentResponse response = appointmentService.updateAppointmentStatus(id, status);
//    return ResponseEntity.ok(response);
//}
//
//@PutMapping("/{id}/update-service-time-frame")
//@PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc')")
//public ResponseEntity<AppointmentResponse> updateServiceTimeFrameId(
//        @PathVariable String id, @RequestBody Map<String, String> request) {
//    String serviceTimeFrameId = request.get("serviceTimeFrameId");
//    AppointmentResponse response = appointmentService.updateServiceTimeFrameId(id, serviceTimeFrameId);
//    return ResponseEntity.ok(response);
//}
//
//@PutMapping("/{id}/update-replacement-doctor")
//@PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc')")
//public ResponseEntity<AppointmentResponse> updateReplacementDoctorId(
//        @PathVariable String id, @RequestBody Map<String, String> request) {
//    String replacementDoctorId = request.get("replacementDoctorId");
//    AppointmentResponse response = appointmentService.updateReplacementDoctorId(id, replacementDoctorId);
//    return ResponseEntity.ok(response);
//}
//
//@PutMapping("/{id}/update-order-number")
//@PreAuthorize("hasRole('QuanLyLichKhamBenh') or hasRole('GiamDoc')")
//public ResponseEntity<AppointmentResponse> updateAppointmentOrderNumber(
//        @PathVariable String id, @RequestBody Map<String, Integer> request) {
//    Integer orderNumber = request.get("orderNumber");
//    AppointmentResponse response = appointmentService.updateAppointmentOrderNumber(id, orderNumber);
//    return ResponseEntity.ok(response);
//}