package com.programmingtechie.doctor_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.doctor_service.dto.response.DoctorResponse;
import com.programmingtechie.doctor_service.dto.response.PageResponse;
import com.programmingtechie.doctor_service.service.DoctorServiceV1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/doctor")
@RequiredArgsConstructor
@Slf4j
public class DoctorControllerV1 {
    final DoctorServiceV1 doctorServiceV1;

    // Lấy danh sách bác sĩ với phân trang
    @GetMapping("/get-all")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc') or hasRole('NguoiDung')")
    public PageResponse<DoctorResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceV1.getAll(page, size);
    }

    @GetMapping("/public/get-all")
    public PageResponse<DoctorResponse> getAllByCustomer(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceV1.getAllDoctorsWithServiceTimeFrame(page, size);
    }

    // Lấy bác sĩ theo id qua GET
    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public DoctorResponse getById(@PathVariable String id) {
        return doctorServiceV1.getById(id);
    }

    // Lấy bác sĩ theo id qua GET
    @GetMapping("/public/id/{id}")
    public DoctorResponse getByIdByCustomer(@PathVariable String id) {
        return doctorServiceV1.getById(id);
    }

    // Lấy bác sĩ theo id qua POST
    @PostMapping("/id")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public DoctorResponse getByIdPost(@RequestBody String id) {
        return doctorServiceV1.getById(id);
    }

    // Lấy bác sĩ theo số điện thoại qua GET
    @GetMapping("/phone/{phoneNumber}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public DoctorResponse getByPhoneNumber(@PathVariable String phoneNumber) {
        return doctorServiceV1.getByPhoneNumber(phoneNumber);
    }

    // Lấy bác sĩ theo số điện thoại qua POST
    @PostMapping("/phone")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public DoctorResponse getByPhoneNumberPost(@RequestBody String phoneNumber) {
        return doctorServiceV1.getByPhoneNumber(phoneNumber);
    }

    // Lấy danh sách bác sĩ theo Specialty với phân trang
    @GetMapping("/specialty/{specialtyId}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public PageResponse<DoctorResponse> getDoctorsBySpecialty(
            @PathVariable String specialtyId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceV1.getDoctorsBySpecialty(specialtyId, page, size);
    }

    // Lấy danh sách bác sĩ theo Qualification với phân trang
    @GetMapping("/qualification/{qualificationAbbreviation}")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public PageResponse<DoctorResponse> getDoctorsByQualification(
            @PathVariable String qualificationAbbreviation,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return doctorServiceV1.getDoctorsByQualification(qualificationAbbreviation, page, size);
    }

    // API tìm kiếm bác sĩ theo từ khóa và phân trang
    @GetMapping("/search1")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<DoctorResponse>> searchDoctors1(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(doctorServiceV1.searchDoctors1(keyword, page, size));
    }

    // API tìm kiếm bác sĩ theo từ khóa và phân trang sử dụng extension unaccent
    @GetMapping("/search")
    @PreAuthorize("hasRole('QuanTriVienHeThong') or hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<DoctorResponse>> searchDoctors(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return ResponseEntity.ok(doctorServiceV1.searchDoctors(keyword, page, size));
    }
}
