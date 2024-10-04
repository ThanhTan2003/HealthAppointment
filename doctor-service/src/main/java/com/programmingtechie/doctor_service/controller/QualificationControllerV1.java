package com.programmingtechie.doctor_service.controller;

import com.programmingtechie.doctor_service.dto.response.PageResponse;
import com.programmingtechie.doctor_service.dto.response.QualificationResponse;
import com.programmingtechie.doctor_service.service.QualificationServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/doctor/qualification")
@RequiredArgsConstructor
@Slf4j
public class QualificationControllerV1 {
    final QualificationServiceV1 qualificationServiceV1;

    // Lấy tất cả qualifications với phân trang
    @GetMapping("/get-all")
    public ResponseEntity<PageResponse<QualificationResponse>> getAllQualifications(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(qualificationServiceV1.getAllQualifications(page, size));
    }

    // Lấy qualification theo abbreviation
    @GetMapping("/abbreviation/{abbreviation}")
    public ResponseEntity<QualificationResponse> getQualificationByAbbreviation(@PathVariable String abbreviation) {
        Optional<QualificationResponse> qualification = qualificationServiceV1.getQualificationByAbbreviation(abbreviation);
        return qualification.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
