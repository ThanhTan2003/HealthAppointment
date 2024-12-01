package com.programmingtechie.patient_service.mapper;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.programmingtechie.patient_service.dto.request.PatientCreationRequest;
import com.programmingtechie.patient_service.dto.response.PatientAndCustomerInfoResponse;
import com.programmingtechie.patient_service.dto.response.PatientResponse;
import com.programmingtechie.patient_service.dto.response.customer.CustomerIdentityResponse;
import com.programmingtechie.patient_service.model.Patient;
import com.programmingtechie.patient_service.repository.httpClient.CustomerIdentityClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PatientMapper {
    private final CustomerIdentityClient customerIdentityClient;

    public PatientResponse mapToPatientResponse(Patient patient) {
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
                .customerId(patient.getCustomerId())
                .lastUpdated(LocalDateTime.now())
                .build();
    }

    public Patient mapToPatientRequest(PatientCreationRequest patientRequest) {
        var context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Người dùng chưa được xác thực");
        }

        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
            // Lấy thông tin từ Jwt
            String id = jwt.getClaim("id");
            if (id == null) {
                throw new IllegalArgumentException("Không tìm thấy ID trong token!");
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
                    .customerId(id)
                    .lastUpdated(LocalDateTime.now())
                    .build();
        }
        throw new IllegalArgumentException("Principal không hợp lệ hoặc không phải là JWT");
    }

    // Tìm thông tin khách hàng theo customer id trong hồ sơ khám bệnh
    public PatientAndCustomerInfoResponse toPatientResponse(Patient patient) {
        PatientAndCustomerInfoResponse patientResponse = PatientAndCustomerInfoResponse.builder()
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
                .customerId(patient.getCustomerId())
                .lastUpdated(LocalDateTime.now())
                .build();

        // try {
        // log.info("chay......");
        CustomerIdentityResponse customerIdentityResponse =
                customerIdentityClient.getByCustomerId(patientResponse.getCustomerId());
        patientResponse.setCustomerIdentityResponse(customerIdentityResponse);
        log.info(customerIdentityResponse.toString());
        // } catch (Exception e) {
        // log.error("Connect error to Customer service: " + e.toString());
        // }
        return patientResponse;
    }

    // Tạo mã ngẫu nhiên
    private static String generatePatientID() {
        String prefix = "BN-";
        String middlePart = generateRandomDigits(0); // 6 chữ số ngẫu nhiên
        String suffixPart = generateRandomAlphanumeric(20); // 6 ký tự chữ hoa hoặc số ngẫu nhiên
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
        String characters = "06BDYZVR2XJAW5KLTQSI9MC8UHE1OFG34NP7";
        Random random = new Random();
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < length; i++) {
            result.append(characters.charAt(random.nextInt(characters.length())));
        }
        return result.toString();
    }
    // Kết thúc tạo mã ngẫu nhiên
}
