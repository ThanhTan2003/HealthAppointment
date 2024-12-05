package com.programmingtechie.patient_service.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.patient_service.dto.request.PatientCreationRequest;
import com.programmingtechie.patient_service.dto.request.PatientUpdateRequest;
import com.programmingtechie.patient_service.dto.response.CustomerWithPatientDetailsResponse;
import com.programmingtechie.patient_service.dto.response.PageResponse;
import com.programmingtechie.patient_service.dto.response.PatientAndCustomerInfoResponse;
import com.programmingtechie.patient_service.dto.response.PatientResponse;
import com.programmingtechie.patient_service.service.PatientServiceV1;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/patient")
@RequiredArgsConstructor
public class PatientController {

    final PatientServiceV1 patientServiceV1;

    @PostMapping("/create-patient")
    @PreAuthorize("hasRole('NguoiDung')")
    public PatientResponse createPatient(@RequestBody PatientCreationRequest patientRequest) {
        return patientServiceV1.createPatient(patientRequest);
    }

    @PutMapping("/update-patient/{id}")
    @PostAuthorize("" + "hasRole('NguoiDung') or "
            + "hasRole('GiamDoc') or "
            + "returnObject.email == authentication.principal.claims['email']")
    public PatientResponse updatePatient(
            @PathVariable("id") String patientId, @RequestBody PatientUpdateRequest patientUpdateRequest) {
        return patientServiceV1.updatePatient(patientId, patientUpdateRequest);
    }

    @DeleteMapping
    @PostAuthorize("" + "hasRole('NguoiDung') or "
            + "hasRole('GiamDoc') or "
            + "returnObject.email == authentication.principal.claims['email']")
    public String deletePatient(String patientID) {
        patientServiceV1.deletePatient(patientID);
        return "Xóa hồ sơ thành công";
    }

    @GetMapping("/id/{patientId}")
    @PreAuthorize(
            "hasRole('QuanTriVienHeThong') or hasRole('NguoiDung') or returnObject.email == authentication.principal.claims['email']")
    public PatientResponse getByPatientId(@PathVariable String patientId) {
        return patientServiceV1.getByPatientId(patientId);
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or "
            + "hasRole('QuanTriVienHeThong') or "
            + "hasRole('NguoiDung') or "
            + "returnObject.email == authentication.principal.claims['email']")
    public PageResponse<PatientResponse> getPatientByCustomerId(
            @PathVariable("customerId") String customerId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return patientServiceV1.getPatientByCustomerId(customerId, page, size);
    }

    @GetMapping("/customer/patient-details")
    @PreAuthorize(
            "hasRole('QuanTriVienHeThong') or hasRole('NguoiDung') or returnObject.email == authentication.principal.claims['email']")
    public PageResponse<PatientResponse> getMyPatientRecord(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return patientServiceV1.getMyPatientRecord(page, size);
    }

    @GetMapping("/customer/unbooked-patient-details")
    @PreAuthorize(
            "hasRole('QuanTriVienHeThong') or hasRole('NguoiDung') or returnObject.email == authentication.principal.claims['email']")
    public PageResponse<PatientResponse> getUnbookedPatientRecords(
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size,
            @RequestParam String serviceTimeFrameId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return patientServiceV1.getUnbookedPatientRecords(serviceTimeFrameId, date, page, size);
    }

    @GetMapping("/customer/patient-details/{customerId}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or "
            + "hasRole('QuanTriVienHeThong') or "
            + "hasRole('NguoiDung') or "
            + "returnObject.email == authentication.principal.claims['email']")
    public CustomerWithPatientDetailsResponse getCustomerWithPatientDetails(
            @PathVariable("customerId") String customerId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return patientServiceV1.getCustomerWithPatientDetails(customerId, page, size);
    }

    @GetMapping("/customer/info/{customerId}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc') or " + "hasRole('NguoiDung')")
    public PageResponse<PatientAndCustomerInfoResponse> getPatientWithCustomerInfo(
            @PathVariable("customerId") String customerId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return patientServiceV1.getPatientWithCustomerInfo(customerId, page, size);
    }

    @PostMapping("/customer/email")
    @PreAuthorize(
            "hasRole('NguoiDung') or hasRole('QuanTriVienHeThong') or returnObject.email == authentication.principal.claims['email']")
    public PageResponse<PatientResponse> getPatientByCustomerEmail(
            @RequestParam String email,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return patientServiceV1.getPatientByCustomerEmail(email, page, size);
    }

    @PostMapping("/customer/phone-number")
    @PreAuthorize(
            "hasRole('NguoiDung') or hasRole('QuanTriVienHeThong') or returnObject.email == authentication.principal.claims['email']")
    public PageResponse<PatientResponse> getPatientByCustomerPhoneNumber(
            @RequestParam String phoneNumber,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return patientServiceV1.getPatientByCustomerPhoneNumber(phoneNumber, page, size);
    }

    @GetMapping("/exists/{id}")
    @PreAuthorize("hasRole('NguoiDung')")
    public ResponseEntity<Boolean> checkPatientExists(@PathVariable String id) {
        boolean exists = patientServiceV1.doesPatientExist(id);
        return ResponseEntity.ok(exists);
    }
}
