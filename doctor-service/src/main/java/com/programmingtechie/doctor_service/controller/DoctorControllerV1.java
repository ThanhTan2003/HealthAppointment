package com.programmingtechie.doctor_service.controller;

import com.programmingtechie.doctor_service.dto.response.DoctorResponse;
import com.programmingtechie.doctor_service.dto.response.PageResponse;
import com.programmingtechie.doctor_service.service.DoctorServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class DoctorControllerV1 {
    final DoctorServiceV1 doctorServiceV1;

    // Lấy danh sách bác sĩ với phân trang
    @GetMapping("/get-all")
    public PageResponse<DoctorResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return doctorServiceV1.getAll(page, size);
    }

    // Lấy bác sĩ theo id qua GET
    @GetMapping("/id/{id}")
    public DoctorResponse getById(@PathVariable String id) {
        return doctorServiceV1.getById(id);
    }

    // Lấy bác sĩ theo id qua POST
    @PostMapping("/id")
    public DoctorResponse getByIdPost(@RequestBody String id) {
        return doctorServiceV1.getById(id);
    }

    // Lấy bác sĩ theo số điện thoại qua GET
    @GetMapping("/phone/{phoneNumber}")
    public DoctorResponse getByPhoneNumber(@PathVariable String phoneNumber) {
        return doctorServiceV1.getByPhoneNumber(phoneNumber);
    }

    // Lấy bác sĩ theo số điện thoại qua POST
    @PostMapping("/phone")
    public DoctorResponse getByPhoneNumberPost(@RequestBody String phoneNumber) {
        return doctorServiceV1.getByPhoneNumber(phoneNumber);
    }

    // Lấy danh sách bác sĩ theo Specialty với phân trang
    @GetMapping("/specialty/{specialtyId}")
    public PageResponse<DoctorResponse> getDoctorsBySpecialty(
            @PathVariable String specialtyId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return doctorServiceV1.getDoctorsBySpecialty(specialtyId, page, size);
    }

    // Lấy danh sách bác sĩ theo Qualification với phân trang
    @GetMapping("/qualification/{qualificationAbbreviation}")
    public PageResponse<DoctorResponse> getDoctorsByQualification(
            @PathVariable String qualificationAbbreviation,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return doctorServiceV1.getDoctorsByQualification(qualificationAbbreviation, page, size);
    }
}
