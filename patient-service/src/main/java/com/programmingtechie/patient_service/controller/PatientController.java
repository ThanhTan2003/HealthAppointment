package com.programmingtechie.patient_service.controller;

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
    // Out put example:
    // {
    // "id": "80d053c3-8821-4119-a6b9-460ea2b6dba5",
    // "fullName": "tgt",
    // "dateOfBirth": "2024-11-08T00:00:00.000+00:00",
    // "gender": "Nam",
    // "phoneNumber": "555",
    // "email": "tgt",
    // "status": "Đang hoạt động",
    // "lastAccessTime": null,
    // "lastUpdated": "2024-11-08T14:37:34.111006",
    // "patientDetails": {
    // "totalPages": 1,
    // "currentPage": 1,
    // "pageSize": 2,
    // "totalElements": 2,
    // "data": [
    // {
    // "id": "BN-3297434125CDHXD4C4KR",
    // "fullName": "thinh",
    // "dateOfBirth": "2000-02-02",
    // "gender": "Nam",
    // "insuranceId": "1222",
    // "identificationCode": "22222",
    // "nation": "VietNam",
    // "occupation": "SinhVien",
    // "phoneNumber": "5552",
    // "email": "tgt",
    // "country": "VietNam",
    // "province": "BenTre",
    // "district": "TP.BenTre",
    // "ward": "PK",
    // "address": "BenTre",
    // "relationship": "Me",
    // "note": "none",
    // "lastUpdated": "2024-11-12T16:12:09.9692267",
    // "customerId": "80d053c3-8821-4119-a6b9-460ea2b6dba5"
    // },
    // {
    // "id": "BN-7525982749XYLH2O5X0N",
    // "fullName": "thinh",
    // "dateOfBirth": "2000-02-02",
    // "gender": "Nam",
    // "insuranceId": "1222",
    // "identificationCode": "22222",
    // "nation": "VietNam",
    // "occupation": "SinhVien",
    // "phoneNumber": "5555",
    // "email": "tgt",
    // "country": "VietNam",
    // "province": "BenTre",
    // "district": "TP.BenTre",
    // "ward": "PK",
    // "address": "BenTre",
    // "relationship": "Me",
    // "note": "none",
    // "lastUpdated": "2024-11-12T16:12:09.9692267",
    // "customerId": "80d053c3-8821-4119-a6b9-460ea2b6dba5"
    // }
    // ]
    // }
    // }

    @GetMapping("/customer/info/{customerId}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc') or " + "hasRole('NguoiDung')")
    public PageResponse<PatientAndCustomerInfoResponse> getPatientWithCustomerInfo(
            @PathVariable("customerId") String customerId,
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return patientServiceV1.getPatientWithCustomerInfo(customerId, page, size);
    }
    // Out put example:
    // {
    // "totalPages": 1,
    // "currentPage": 1,
    // "pageSize": 4,
    // "totalElements": 2,
    // "data": [
    // {
    // "id": "BN-3297434125CDHXD4C4KR",
    // "fullName": "thinh",
    // "dateOfBirth": "2000-02-02",
    // "gender": "Nam",
    // "insuranceId": "1222",
    // "identificationCode": "22222",
    // "nation": "VietNam",
    // "occupation": "SinhVien",
    // "phoneNumber": "5552",
    // "email": "tgt",
    // "country": "VietNam",
    // "province": "BenTre",
    // "district": "TP.BenTre",
    // "ward": "PK",
    // "address": "BenTre",
    // "relationship": "Me",
    // "note": "none",
    // "lastUpdated": "2024-11-12T16:04:15.6161553",
    // "customerId": "80d053c3-8821-4119-a6b9-460ea2b6dba5",
    // "customerIdentityResponse": {
    // "id": "80d053c3-8821-4119-a6b9-460ea2b6dba5",
    // "fullName": "tgt",
    // "dateOfBirth": "2024-11-08T00:00:00.000+00:00",
    // "gender": "Nam",
    // "phoneNumber": "555",
    // "email": "tgt",
    // "status": "Đang hoạt động",
    // "lastAccessTime": null,
    // "lastUpdated": "2024-11-08T14:37:34.111006"
    // }
    // },
    // {
    // "id": "BN-7525982749XYLH2O5X0N",
    // "fullName": "thinh",
    // "dateOfBirth": "2000-02-02",
    // "gender": "Nam",
    // "insuranceId": "1222",
    // "identificationCode": "22222",
    // "nation": "VietNam",
    // "occupation": "SinhVien",
    // "phoneNumber": "5555",
    // "email": "tgt",
    // "country": "VietNam",
    // "province": "BenTre",
    // "district": "TP.BenTre",
    // "ward": "PK",
    // "address": "BenTre",
    // "relationship": "Me",
    // "note": "none",
    // "lastUpdated": "2024-11-12T16:04:15.8081545",
    // "customerId": "80d053c3-8821-4119-a6b9-460ea2b6dba5",
    // "customerIdentityResponse": {
    // "id": "80d053c3-8821-4119-a6b9-460ea2b6dba5",
    // "fullName": "tgt",
    // "dateOfBirth": "2024-11-08T00:00:00.000+00:00",
    // "gender": "Nam",
    // "phoneNumber": "555",
    // "email": "tgt",
    // "status": "Đang hoạt động",
    // "lastAccessTime": null,
    // "lastUpdated": "2024-11-08T14:37:34.111006"
    // }
    // }
    // ]
    // }

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
