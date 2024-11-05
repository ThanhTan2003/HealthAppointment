package com.programmingtechie.customer_service.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.customer_service.dto.request.PatientRequest;

@RestController
@RequestMapping("/api/v1/customer/patient")
@CrossOrigin("*")
public class PatientController {

    @PostMapping("/create-patient")
    public void createPatient(@RequestBody PatientRequest patientRequest, @RequestBody String id) {}
}
