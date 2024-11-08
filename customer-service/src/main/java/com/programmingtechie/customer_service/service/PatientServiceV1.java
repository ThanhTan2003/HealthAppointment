package com.programmingtechie.customer_service.service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.customer_service.dto.request.PatientCreationRequest;
import com.programmingtechie.customer_service.dto.response.PageResponse;
import com.programmingtechie.customer_service.dto.response.PatientResponse;
import com.programmingtechie.customer_service.model.Patient;
import com.programmingtechie.customer_service.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PatientServiceV1 {

    final PatientRepository patientRepository;

    public PatientResponse createPatient(PatientCreationRequest patientRequest) {
        Patient patient = mapToPatientRequest(patientRequest);
        patientRepository.save(patient);
        return mapToPatientResponse(patient);
    }

    public boolean isValidpatient(PatientCreationRequest patientRequest) {
        return isValidInsuranceId(patientRequest.getInsuranceId())
                && isValidIdentificationCode(patientRequest.getIdentificationCode())
                && isValidPhoneNumber(patientRequest.getPhoneNumber());
    }

    private boolean isValidInsuranceId(String insuranceId) {
        return insuranceId != null && insuranceId.length() == 15;
    }

    private boolean isValidIdentificationCode(String identificationCode) {
        return identificationCode != null && identificationCode.length() == 12;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.length() == 10;
    }

    private PatientResponse mapToPatientResponse(Patient patient) {
        return PatientResponse.builder()
                .id(patient.getId())
                .fullName(patient.getFullName())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender())
                .insuranceId(patient.getInsuranceId())
                .identificationCode(patient.getIdentificationCode())
                .nation(patient.getNation())
                .occupation(patient.getOccupation())
                .phoneNumber(patient.getPhoneNumber())
                .email(patient.getEmail())
                .country(patient.getCountry())
                .province(patient.getProvince())
                .district(patient.getDistrict())
                .ward(patient.getWard())
                .address(patient.getAddress())
                .relationship(patient.getRelationship())
                .note(patient.getNote())
                .lastUpdated(LocalDateTime.now())
                .customerId(patient.getCustomerId())
                .build();
    }

    public Patient mapToPatientRequest(PatientCreationRequest patientRequest) {
        if (!isValidpatient(patientRequest)) {
            throw new IllegalArgumentException("Invalid patient request");
        }

        return Patient.builder()
                .id(generatePatientID())
                .fullName(patientRequest.getFullName())
                .dateOfBirth(patientRequest.getDateOfBirth())
                .gender(patientRequest.getGender())
                .insuranceId(patientRequest.getInsuranceId())
                .identificationCode(patientRequest.getIdentificationCode())
                .nation(patientRequest.getNation())
                .occupation(patientRequest.getOccupation())
                .phoneNumber(patientRequest.getPhoneNumber())
                .email(patientRequest.getEmail())
                .country(patientRequest.getCountry())
                .province(patientRequest.getProvince())
                .district(patientRequest.getDistrict())
                .ward(patientRequest.getWard())
                .address(patientRequest.getAddress())
                .relationship(patientRequest.getRelationship())
                .note(patientRequest.getNote())
                .lastUpdated(LocalDateTime.now())
                .customerId(patientRequest.getCustomerId())
                .build();
    }

    public PageResponse<PatientResponse> getPatientByCustomerId(String customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Patient> pageData = patientRepository.findPatientByCustomerId(customerId, pageable);
        return PageResponse.<PatientResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(this::mapToPatientResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public PageResponse<PatientResponse> getPatientByCustomerEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Patient> pageData = patientRepository.findPatientByCustomerEmail(email, pageable);
        return PageResponse.<PatientResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(this::mapToPatientResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public PageResponse<PatientResponse> getPatientByCustomerPhoneNumber(String phoneNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Patient> pageData = patientRepository.findPatientByCustomerPhoneNumber(phoneNumber, pageable);
        return PageResponse.<PatientResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(this::mapToPatientResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // Tạo mã ngẫu nhiên
    private static String generatePatientID() {
        String prefix = "BN-";
        String middlePart = generateRandomDigits(10); // 6 chữ số ngẫu nhiên
        String suffixPart = generateRandomAlphanumeric(10); // 6 ký tự chữ hoa hoặc số ngẫu nhiên
        return prefix + middlePart + suffixPart;
    }

    private static String generateRandomDigits(int length) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(random.nextInt(10)); // Tạo số ngẫu nhiên từ 0 đến 9
        }
        return result.toString();
    }

    private static String generateRandomAlphanumeric(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }
    // Kết thúc tạo mã ngẫu nhiên

}
