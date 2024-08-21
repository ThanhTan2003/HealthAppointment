package com.programmingtechie.doctor_service.controller;

import com.programmingtechie.doctor_service.service.DoctorSpecialtyServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/doctor/doctor-specialty")
@RequiredArgsConstructor
@Slf4j
public class DoctorSpecialtyControllerV1
{
    final DoctorSpecialtyServiceV1 doctorSpecialtyServiceV1;
}
