package com.programmingtechie.customer_service.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.customer_service.dto.request.PatientRequest;
import com.programmingtechie.customer_service.dto.response.PatientResponse;
import com.programmingtechie.customer_service.service.PatientServiceV1;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customer/patient")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PatientController {

    final PatientServiceV1 patientServiceV1;

    @PostMapping("/create-patient")
    @PreAuthorize("hasRole('NguoiDung') or returnObject.email == authentication.principal.claims['email']")
    public PatientResponse createPatient(@RequestBody PatientRequest patientRequest, @RequestBody String id) {
        return patientServiceV1.createPatient(patientRequest);
    }
}
