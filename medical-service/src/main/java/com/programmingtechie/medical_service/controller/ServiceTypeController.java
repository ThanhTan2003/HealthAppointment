package com.programmingtechie.medical_service.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.medical_service.dto.request.ServiceTypeRequest;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.dto.response.ServiceTypeResponse;
import com.programmingtechie.medical_service.service.ServiceTypeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medical/service-type")
@RequiredArgsConstructor
public class ServiceTypeController {
    private final ServiceTypeService serviceTypeService;

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
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc') or " + "hasRole('NguoiDung')")
    public ResponseEntity<PageResponse<ServiceTypeResponse>> getAllServiceTypes(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceTypeService.getAllServiceTypes(page, size));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc') or " + "hasRole('NguoiDung')")
    public ResponseEntity<ServiceTypeResponse> getServiceTypeById(@PathVariable String id) {
        return ResponseEntity.ok(serviceTypeService.getServiceTypeById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<ServiceTypeResponse> createServiceType(@RequestBody ServiceTypeRequest serviceTypeRequest) {
        return ResponseEntity.ok(serviceTypeService.createServiceType(serviceTypeRequest));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<ServiceTypeResponse> updateServiceType(
            @PathVariable String id, @RequestBody ServiceTypeRequest serviceTypeRequest) {
        return ResponseEntity.ok(serviceTypeService.updateServiceType(id, serviceTypeRequest));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<Void> deleteServiceType(@PathVariable String id) {
        serviceTypeService.deleteServiceType(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc') or " + "hasRole('NguoiDung')")
    public ResponseEntity<PageResponse<ServiceTypeResponse>> searchServiceTypes(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceTypeService.searchServiceTypes(keyword, page, size));
    }
}
