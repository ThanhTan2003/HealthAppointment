package com.programmingtechie.customer_service.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.customer_service.dto.request.PatientRequest;
import com.programmingtechie.customer_service.service.PatientServiceV1;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customer/patient")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PatientController {
    final PatientServiceV1 patientServiceV1;

    @PostMapping("/create-patient")
    public void createPatient(@RequestBody PatientRequest patientRequest, @RequestBody String id) {
        patientServiceV1.createPatient(patientRequest, id);
    }
}
