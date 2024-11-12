package com.programmingtechie.identity_service.mapper.Patient;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.programmingtechie.identity_service.dto.response.Customer.CustomerPatientResponse;
import com.programmingtechie.identity_service.dto.response.Customer.CustomerResponse;
import com.programmingtechie.identity_service.dto.response.PageResponse;
import com.programmingtechie.identity_service.dto.response.Patient.PatientDetailsResponse;
import com.programmingtechie.identity_service.model.Customer;
import com.programmingtechie.identity_service.repository.httpClient.Patient.PatientClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PatientDetailsMapper {

    private final PatientClient patientClient;

    public CustomerPatientResponse mapToCustomerPatientResponse(Customer customer, int page, int size) {
        CustomerResponse customerResponse = CustomerResponse.builder()
                .id(customer.getId())
                .fullName(customer.getFullName())
                .dateOfBirth(customer.getDateOfBirth())
                .gender(customer.getGender())
                .phoneNumber(customer.getPhoneNumber())
                .email(customer.getEmail())
                .status(customer.getStatus())
                .lastAccessTime(customer.getLastAccessTime())
                .lastUpdated(LocalDateTime.now())
                .build();
        PageResponse<PatientDetailsResponse> patientDetailsResponse =
                patientClient.getPatientDetailsByCustomerId(customer.getId(), page, size);

        return CustomerPatientResponse.builder()
                .id(customerResponse.getId())
                .fullName(customerResponse.getFullName())
                .dateOfBirth(customerResponse.getDateOfBirth())
                .gender(customerResponse.getGender())
                .phoneNumber(customerResponse.getPhoneNumber())
                .email(customerResponse.getEmail())
                .status(customerResponse.getStatus())
                .lastAccessTime(customerResponse.getLastAccessTime())
                .lastUpdated(customerResponse.getLastUpdated())
                .patientDetails(patientDetailsResponse)
                .build();
    }
}
