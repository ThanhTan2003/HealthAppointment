package com.programmingtechie.doctor_service.controller;

import com.programmingtechie.doctor_service.dto.request.DoctorRequest;
import com.programmingtechie.doctor_service.dto.response.DoctorResponse;
import com.programmingtechie.doctor_service.dto.response.PageResponse;
import com.programmingtechie.doctor_service.service.DoctorServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
@RequiredArgsConstructor
@Slf4j
public class DoctorControllerV1 {
    final DoctorServiceV1 doctorServiceV1;

    // Tao thong tin bac si moi
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public void createDoctor(@RequestBody DoctorRequest doctorRequest) {
        doctorServiceV1.createDoctor(doctorRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DoctorResponse> getAll() {
        return doctorServiceV1.getAll();
    }

    // Lay danh sach bac si
    @GetMapping("/get-all")
    public PageResponse<DoctorResponse> getAll(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ) {
        return doctorServiceV1.getAll(page, size);
    }

    // Lay theo id
    @GetMapping("/id/{id}")
    public DoctorResponse geById(@PathVariable String id) {
        return doctorServiceV1.getById(id);
    }

    // Lay theo id
    @PostMapping("/id")
    public DoctorResponse geById1(@RequestBody String id) {
        return doctorServiceV1.getById(id);
    }

    // Lay theo phone
    @GetMapping("/phone/{phone}")
    public DoctorResponse getByPhoneNumber(@PathVariable String phoneNumber) {
        return doctorServiceV1.getByPhoneNumber(phoneNumber);
    }

    // Lay theo phone
    @PostMapping("/phone")
    public DoctorResponse getByPhoneNumber1(@RequestBody String phoneNumber) {
        return doctorServiceV1.getByPhoneNumber(phoneNumber);
    }

    // Lay theo email
    @PostMapping("/email")
    public DoctorResponse getByEmail(@RequestBody String email) {
        return doctorServiceV1.getByEmail(email);
    }

    // Lay theo email
    @PostMapping("/gender")
    public DoctorResponse getByGender(@RequestBody String gender) {
        return doctorServiceV1.getByGender(gender);
    }

    // Cap nhat thong tin bac si
    @PutMapping("update/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateDoctor(@PathVariable String id, @RequestBody DoctorRequest doctorRequest)
    {
        doctorServiceV1.updateDoctor(id, doctorRequest);
    }

    // Xoa thong tin bac si
    @DeleteMapping("delete/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteDoctor(@PathVariable String id)
    {
        doctorServiceV1.deleteDoctor(id);
    }
}
