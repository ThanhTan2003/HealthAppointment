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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createCustomer(@RequestBody DoctorRequest productRequest)
    {
        for(int i = 0; i < 200000; i++)
            doctorServiceV1.createDoctor(productRequest);
        return "Đã thêm thông tin bác sĩ mới thành công!";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DoctorResponse> getAll()
    {
        return doctorServiceV1.getAll();
    }

    @GetMapping("/get-all")
    public PageResponse<DoctorResponse> getAll (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    )
    {
        return doctorServiceV1.getAll(page, size);
    }
}
