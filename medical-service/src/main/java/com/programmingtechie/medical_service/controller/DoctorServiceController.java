package com.programmingtechie.medical_service.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.medical_service.dto.request.DoctorServiceRequest;
import com.programmingtechie.medical_service.dto.response.DoctorServiceResponse;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.service.DoctorServiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medical/doctor-service")
@RequiredArgsConstructor
public class DoctorServiceController {
    private final DoctorServiceService doctorServiceService;

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
    public ResponseEntity<PageResponse<DoctorServiceResponse>> getAllDoctorServices(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(doctorServiceService.getAllDoctorServices(page, size));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc') or hasRole('NguoiDung')")
    public ResponseEntity<DoctorServiceResponse> getDoctorServiceById(@PathVariable String id) {
        return ResponseEntity.ok(doctorServiceService.getDoctorServiceById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<DoctorServiceResponse> createDoctorService(
            @RequestBody DoctorServiceRequest doctorServiceRequest) {
        return ResponseEntity.ok(doctorServiceService.createDoctorService(doctorServiceRequest));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<DoctorServiceResponse> updateDoctorService(
            @PathVariable String id, @RequestBody DoctorServiceRequest doctorServiceRequest) {
        return ResponseEntity.ok(doctorServiceService.updateDoctorService(id, doctorServiceRequest));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<Void> deleteDoctorService(@PathVariable String id) {
        doctorServiceService.deleteDoctorService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/doctor-id/{doctorId}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc') or hasRole('NguoiDung')")
    public ResponseEntity<PageResponse<DoctorServiceResponse>> getDoctorServicesByDoctorId(
            @PathVariable String doctorId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(doctorServiceService.getDoctorServicesByDoctorId(doctorId, page, size));
    }

    @GetMapping("/public/doctor-id/{doctorId}")
    public ResponseEntity<PageResponse<DoctorServiceResponse>> getDoctorServicesByDoctorIdPublic(
            @PathVariable String doctorId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(doctorServiceService.getDoctorServicesByDoctorId(doctorId, page, size));
    }
}
