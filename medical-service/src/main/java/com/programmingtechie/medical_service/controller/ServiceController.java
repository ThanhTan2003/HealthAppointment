package com.programmingtechie.medical_service.controller;

import com.programmingtechie.medical_service.dto.request.ServiceRequest;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.dto.response.ServiceResponse;
import com.programmingtechie.medical_service.service.ServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medical/service")
@RequiredArgsConstructor
public class ServiceController {
    private final ServiceService serviceService;

    @GetMapping("/get-all")
    public ResponseEntity<PageResponse<ServiceResponse>> getAllServices(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceService.getAllServices(page, size));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ServiceResponse> getServiceById(@PathVariable String id) {
        return ResponseEntity.ok(serviceService.getServiceById(id));
    }

    @GetMapping("/service-type/{serviceTypeId}")
    public ResponseEntity<PageResponse<ServiceResponse>> getServicesByServiceTypeId(
            @PathVariable String serviceTypeId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceService.getServicesByServiceTypeId(serviceTypeId, page, size));
    }

    @GetMapping("/specialty/{specialtyId}")
    public ResponseEntity<PageResponse<ServiceResponse>> getServicesBySpecialtyId(
            @PathVariable String specialtyId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceService.getServicesBySpecialtyId(specialtyId, page, size));
    }

    @PostMapping("/create")
    public ResponseEntity<ServiceResponse> createService(@RequestBody ServiceRequest serviceRequest) {
        return ResponseEntity.ok(serviceService.createService(serviceRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ServiceResponse> updateService(
            @PathVariable String id,
            @RequestBody ServiceRequest serviceRequest) {
        return ResponseEntity.ok(serviceService.updateService(id, serviceRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable String id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ServiceResponse>> searchServices(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceService.searchServices(keyword, page, size));
    }
}