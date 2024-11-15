package com.programmingtechie.medical_service.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.medical_service.dto.request.ServiceRequest;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.dto.response.ServiceResponse;
import com.programmingtechie.medical_service.service.ServiceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medical/service")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;

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
    @PreAuthorize("" +
            "hasRole('QuanTriVienHeThong') or " +
            "hasRole('GiamDoc') or " +
            "hasRole('NguoiDung')")
    public ResponseEntity<PageResponse<ServiceResponse>> getAllServices(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceService.getAllServices(page, size));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc') or " + "hasRole('NguoiDung')")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable String id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @GetMapping("/public/id/{id}")
    public ResponseEntity<ServiceResponse> getServiceByIdByCustomer(@PathVariable String id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @GetMapping("/service-type/{serviceTypeId}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc') or " + "hasRole('NguoiDung')")
    public ResponseEntity<PageResponse<ServiceResponse>> getServicesByServiceTypeId(
            @PathVariable String serviceTypeId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceService.getServicesByServiceTypeId(serviceTypeId, page, size));
    }

    @GetMapping("/public/service-type/{serviceTypeId}")
    public ResponseEntity<PageResponse<ServiceResponse>> getServicesByServiceTypeIdByCustomer(
            @PathVariable String serviceTypeId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceService.getServicesByServiceTypeId(serviceTypeId, page, size));
    }

    @GetMapping("/specialty/{specialtyId}")
    @PreAuthorize("" +
            "hasRole('QuanTriVienHeThong') or " +
            "hasRole('GiamDoc') or " +
            "hasRole('NguoiDung')")
    public ResponseEntity<PageResponse<ServiceResponse>> getServicesBySpecialtyId(
            @PathVariable String specialtyId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceService.getServicesBySpecialtyId(specialtyId, page, size));
    }

    @GetMapping("/public/specialty/{specialtyId}")
    public ResponseEntity<PageResponse<ServiceResponse>> getServicesBySpecialtyIdPublic(
            @PathVariable String specialtyId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceService.getServicesBySpecialtyId(specialtyId, page, size));
    }

    @PostMapping("/create")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<ServiceResponse> createService(@RequestBody ServiceRequest serviceRequest) {
        return ResponseEntity.ok(serviceService.createService(serviceRequest));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<ServiceResponse> updateService(
            @PathVariable String id, @RequestBody ServiceRequest serviceRequest) {
        return ResponseEntity.ok(serviceService.updateService(id, serviceRequest));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<Void> deleteService(@PathVariable String id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<ServiceResponse>> searchServices(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceService.searchServices(keyword, page, size));
    }

    // Lấy danh sách dịch vụ theo chuyên khoa
    @GetMapping("/specialty/not-null")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<ServiceResponse>> getServicesWithSpecialtyIdNotNull(
            @RequestParam(value = "doctor-id") String doctorId,
            @RequestParam(value = "specialty-id") String specialtyId,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(
                serviceService.getServicesWithSpecialtyIdNotNull(doctorId, specialtyId, keyword, page, size));
    }

    // Lấy danh sách dịch vụ ngoài chuyên khoa
    @GetMapping("/service-type/not-null")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<ServiceResponse>> getServicesWithServiceTypeNotNull(
            @RequestParam(value = "doctor-id") String doctorId,
            @RequestParam(value = "service-type-id") String serviceTypeId,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(
                serviceService.getServicesWithServiceTypeNotNull(doctorId, serviceTypeId, keyword, page, size));
    }

    @PostMapping("/get-by-ids")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<List<ServiceResponse>> getServicesByIds(@RequestBody List<String> specialtyIds) {
        return ResponseEntity.ok(serviceService.getServicesByIds(specialtyIds));
    }
}
