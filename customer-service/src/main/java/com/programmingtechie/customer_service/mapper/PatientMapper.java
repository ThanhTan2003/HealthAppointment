package com.programmingtechie.customer_service.mapper;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.programmingtechie.customer_service.dto.response.CustomerIdentityResponse;
import com.programmingtechie.customer_service.dto.response.PatientAndCustomerInfoResponse;
import com.programmingtechie.customer_service.model.Patient;
import com.programmingtechie.customer_service.repository.httpClient.CustomerIdentityClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PatientMapper {
    private final CustomerIdentityClient customerIdentityClient;

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

        try {
            CustomerIdentityResponse customerIdentityResponse =
                    customerIdentityClient.getByCustomerId(patientResponse.getCustomerId());
            patientResponse.setCustomerIdentityResponse(customerIdentityResponse);
        } catch (Exception e) {
            log.error("Connect error to Customer service: " + e.toString());
        }
        return patientResponse;
    }
}
