package com.programmingtechie.doctor_service.controller;

import com.programmingtechie.doctor_service.dto.response.PageResponse;
import com.programmingtechie.doctor_service.dto.response.SpecialtyResponse;
import com.programmingtechie.doctor_service.service.SpecialtyServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/doctor/specialty")
@RequiredArgsConstructor
@Slf4j
public class SpecialtyControllerV1
{
    final SpecialtyServiceV1 specialtyServiceV1;

    // Lấy tất cả specialties với phân trang
    @GetMapping("/get-all")
    public ResponseEntity<PageResponse<SpecialtyResponse>> getAllSpecialties(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(specialtyServiceV1.getAllSpecialties(page, size));
    }

    // Lấy specialty theo id
    @GetMapping("/id/{id}")
    public ResponseEntity<SpecialtyResponse> getSpecialtyById(@PathVariable String id) {
        Optional<SpecialtyResponse> specialty = specialtyServiceV1.getSpecialtyById(id);
        return specialty.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
