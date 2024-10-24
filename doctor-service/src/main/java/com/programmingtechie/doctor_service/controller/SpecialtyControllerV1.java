package com.programmingtechie.doctor_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.doctor_service.service.SpecialtyServiceV1;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/doctor/specialty")
@RequiredArgsConstructor
@Slf4j
public class SpecialtyControllerV1 {
    final SpecialtyServiceV1 specialtyServiceV1;
}
