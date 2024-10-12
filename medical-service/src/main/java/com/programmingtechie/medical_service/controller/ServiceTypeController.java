package com.programmingtechie.medical_service.controller;

import com.programmingtechie.medical_service.dto.request.ServiceTypeRequest;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.dto.response.ServiceTypeResponse;
import com.programmingtechie.medical_service.service.ServiceTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medical/service-type")
@RequiredArgsConstructor
public class ServiceTypeController {
    private final ServiceTypeService serviceTypeService;

    @GetMapping("/get-all")
    public ResponseEntity<PageResponse<ServiceTypeResponse>> getAllServiceTypes(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceTypeService.getAllServiceTypes(page, size));
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ServiceTypeResponse> getServiceTypeById(@PathVariable String id) {
        return ResponseEntity.ok(serviceTypeService.getServiceTypeById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<ServiceTypeResponse> createServiceType(@RequestBody ServiceTypeRequest serviceTypeRequest) {
        return ResponseEntity.ok(serviceTypeService.createServiceType(serviceTypeRequest));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ServiceTypeResponse> updateServiceType(
            @PathVariable String id,
            @RequestBody ServiceTypeRequest serviceTypeRequest) {
        return ResponseEntity.ok(serviceTypeService.updateServiceType(id, serviceTypeRequest));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteServiceType(@PathVariable String id) {
        serviceTypeService.deleteServiceType(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponse<ServiceTypeResponse>> searchServiceTypes(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(serviceTypeService.searchServiceTypes(keyword, page, size));
    }
}