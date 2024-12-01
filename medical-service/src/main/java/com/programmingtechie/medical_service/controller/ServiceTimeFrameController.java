package com.programmingtechie.medical_service.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.medical_service.dto.request.ServiceTimeFrameRequest;
import com.programmingtechie.medical_service.dto.request.ServiceTimeFrameUpdate;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.dto.response.ServiceTimeFrameResponse;
import com.programmingtechie.medical_service.service.ServiceTimeFrameService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medical/service-time-frame")
@RequiredArgsConstructor
public class ServiceTimeFrameController {
    private final ServiceTimeFrameService serviceTimeFrameService;

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

    @GetMapping("/get-all")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc') or hasRole('NguoiDung')")
    public ResponseEntity<PageResponse<ServiceTimeFrameResponse>> getAllServiceTimeFrames(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceTimeFrameService.getAllServiceTimeFrames(page, size));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc') or hasRole('NguoiDung')")
    public ResponseEntity<ServiceTimeFrameResponse> getServiceTimeFrameById(@PathVariable String id) {
        return ResponseEntity.ok(serviceTimeFrameService.getServiceTimeFrameById(id));
    }

    @GetMapping("/public/id/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc') or hasRole('NguoiDung')")
    public ResponseEntity<ServiceTimeFrameResponse> getServiceTimeFrameByIdById(@PathVariable String id) {
        return ResponseEntity.ok(serviceTimeFrameService.getServiceTimeFrameById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<ServiceTimeFrameResponse> createServiceTimeFrame(
            @RequestBody ServiceTimeFrameRequest serviceTimeFrameRequest) {
        return ResponseEntity.ok(serviceTimeFrameService.createServiceTimeFrame(serviceTimeFrameRequest));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<ServiceTimeFrameResponse> updateServiceTimeFrame(
            @PathVariable String id, @RequestBody ServiceTimeFrameUpdate serviceTimeFrameRequest) {
        return ResponseEntity.ok(serviceTimeFrameService.updateServiceTimeFrame(id, serviceTimeFrameRequest));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<Void> deleteServiceTimeFrame(@PathVariable String id) {
        serviceTimeFrameService.deleteServiceTimeFrame(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/get-by-doctor-and-day")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc') or hasRole('NguoiDung')")
    public ResponseEntity<List<ServiceTimeFrameResponse>> getServiceTimeFramesByDoctorIdAndDayOfWeek(
            @RequestParam(value = "doctorId") String doctorId, @RequestParam(value = "dayOfWeek") String dayOfWeek) {
        return ResponseEntity.ok(
                serviceTimeFrameService.getServiceTimeFramesByDoctorIdAndDayOfWeek(doctorId, dayOfWeek));
    }

    @GetMapping("/public/get-doctor-ids")
    public PageResponse<String> getDoctorsWithServiceTimeFrames(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return serviceTimeFrameService.getDoctorsAllServiceTimeFrames(page, size);
    }

    @GetMapping("/public/get-specialty-ids")
    PageResponse<String> getListSpecialtyWithServiceTimeFrames(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return serviceTimeFrameService.getListSpecialtyWithServiceTimeFrames(page, size);
    }

    @GetMapping("/public/available-days")
    public ResponseEntity<List<String>> getAvailableDaysForDoctorService(@RequestParam String doctorServiceId) {
        List<String> availableDays = serviceTimeFrameService.getAvailableDaysForDoctorService(doctorServiceId);
        return ResponseEntity.ok(availableDays);
    }

    // API nhận doctorServiceId và dayOfWeek, trả về danh sách các ServiceTimeFrame
    @GetMapping("/public/get-by-doctor-and-day")
    public ResponseEntity<List<ServiceTimeFrameResponse>> getServiceTimeFramesByDoctorIdAndDayOfWeekByCustomer(
            @RequestParam(value = "doctorId") String doctorId, @RequestParam(value = "dayOfWeek") String dayOfWeek) {
        List<ServiceTimeFrameResponse> response =
                serviceTimeFrameService.getServiceTimeFramesByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/public/get-by-doctor-service-and-day")
    public ResponseEntity<List<ServiceTimeFrameResponse>> getServiceTimeFramesByDoctorServiceIdAndDayOfWeekByCustomer(
            @RequestParam(value = "doctorServiceId") String doctorServiceId,
            @RequestParam(value = "dayOfWeek") String dayOfWeek,
            @RequestParam(value = "day") String day)
            throws ParseException {
        Date parsedDate = new SimpleDateFormat("yyyy-M-d").parse(day);
        List<ServiceTimeFrameResponse> response =
                serviceTimeFrameService.getServiceTimeFramesByDoctorServiceIdAndDayOfWeek(
                        doctorServiceId, dayOfWeek, parsedDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/exists/{id}")
    @PreAuthorize("hasRole('NguoiDung')")
    public ResponseEntity<Boolean> checkServiceTimeFrameExists(@PathVariable String id) {
        boolean exists = serviceTimeFrameService.doesServiceTimeFrameExist(id);
        return ResponseEntity.ok(exists);
    }
}
