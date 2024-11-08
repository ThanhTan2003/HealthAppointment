package com.programmingtechie.customer_service.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.customer_service.dto.request.PatientCreationRequest;
import com.programmingtechie.customer_service.dto.response.PatientResponse;
import com.programmingtechie.customer_service.service.PatientServiceV1;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customer/patient")
@RequiredArgsConstructor
public class PatientController {

    final PatientServiceV1 patientServiceV1;

    @PostMapping("/create-patient")
    @PreAuthorize("hasRole('NguoiDung')")
    public PatientResponse createPatient(@RequestBody PatientCreationRequest patientRequest) {
        return patientServiceV1.createPatient(patientRequest);
    }

    // @GetMapping("/customer")
    // @PreAuthorize(
    //         "hasRole('QuanTriVienHeThong') or hasRole('NguoiDung') or returnObject.email ==
    // authentication.principal.claims['email']")
    // public PageResponse<PatientResponse> getPatientByCustomerId(
    //         @RequestParam String customerId,
    //         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    //         @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
    //     return patientServiceV1.getPatientByCustomerId(customerId, page, size);
    // }

    // @PostMapping("/customer/email")
    // @PreAuthorize(
    //         "hasRole('NguoiDung') or hasRole('QuanTriVienHeThong') or returnObject.email ==
    // authentication.principal.claims['email']")
    // public PageResponse<PatientResponse> getPatientByCustomerEmail(
    //         @RequestParam String email,
    //         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    //         @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
    //     return patientServiceV1.getPatientByCustomerEmail(email, page, size);
    // }

    // @PostMapping("/customer/phone-number")
    // @PreAuthorize(
    //         "hasRole('NguoiDung') or hasRole('QuanTriVienHeThong') or returnObject.email ==
    // authentication.principal.claims['email']")
    // public PageResponse<PatientResponse> getPatientByCustomerPhoneNumber(
    //         @RequestParam String phoneNumber,
    //         @RequestParam(value = "page", required = false, defaultValue = "1") int page,
    //         @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
    //     return patientServiceV1.getPatientByCustomerPhoneNumber(phoneNumber, page, size);
    // }
}
