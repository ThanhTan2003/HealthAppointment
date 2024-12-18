package com.programmingtechie.doctor_service.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.doctor_service.dto.response.PageResponse;
import com.programmingtechie.doctor_service.dto.response.SpecialtyResponse;
import com.programmingtechie.doctor_service.service.SpecialtyServiceV1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/doctor/specialty")
@RequiredArgsConstructor
@Slf4j
public class SpecialtyControllerV1 {
    final SpecialtyServiceV1 specialtyServiceV1;

    // Lấy tất cả specialties với phân trang
    @GetMapping("/get-all")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc') or " + "hasRole('NguoiDung')")
    public ResponseEntity<PageResponse<SpecialtyResponse>> getAllSpecialties(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(specialtyServiceV1.getAllSpecialties(page, size));
    }

    @GetMapping("/public/get-all")
    public ResponseEntity<PageResponse<SpecialtyResponse>> getAllSpecialtiesByCustomer(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(specialtyServiceV1.getAllSpecialtiesByCustomer(page, size));
    }

    // Lấy specialty theo id
    @GetMapping("/id/{id}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc') or " + "hasRole('NguoiDung')")
    public ResponseEntity<SpecialtyResponse> getSpecialtyById(@PathVariable String id) {
        Optional<SpecialtyResponse> specialty = specialtyServiceV1.getSpecialtyById(id);
        return specialty.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound()
                .build());
    }

    @GetMapping("/public/id/{id}")
    public ResponseEntity<SpecialtyResponse> getSpecialtyByIdByCustomer(@PathVariable String id) {
        Optional<SpecialtyResponse> specialty = specialtyServiceV1.getSpecialtyById(id);
        return specialty.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound()
                .build());
    }

    // API tìm kiếm chuyên khoa theo từ khóa và phân trang
    @GetMapping("/search")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc') or " + "hasRole('NguoiDung')")
    public ResponseEntity<PageResponse<SpecialtyResponse>> searchSpecialties(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(specialtyServiceV1.searchSpecialties(keyword, page, size));
    }

    @PostMapping("/public/get-by-ids")
    public ResponseEntity<List<SpecialtyResponse>> getSpecialtiesByIds(@RequestBody List<String> specialtyIds) {
        return ResponseEntity.ok(specialtyServiceV1.getSpecialtiesByIds(specialtyIds));
    }
}
