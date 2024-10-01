package com.programmingtechie.customer_service.service;

import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.programmingtechie.customer_service.dto.request.CustomerRequest;
import com.programmingtechie.customer_service.dto.request.PatientRequest;
import com.programmingtechie.customer_service.model.Customer;
import com.programmingtechie.customer_service.model.Patient;
import com.programmingtechie.customer_service.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientServiceV1 {

    final PatientRepository patientRepository;

    public void createCustomer(PatientRequest patientRequest) {
        validCustomer(patientRequest);
        Patient patient = Patient.builder()
                .fullName(patientRequest.getFullName())
                .dateOfBirth(patientRequest.getDateOfBirth())
                .gender(patientRequest.getGender())
                .email(patientRequest.getEmail())
                .phoneNumber(patientRequest.getPhoneNumber())
                .identificationCodeOrPassport(patientRequest.getIdentificationCodeOrPassport())
                .build();

        patientRepository.save(patient);
    }

    void validCustomer(PatientRequest patientRequest) {
        if (patientRequest.getFullName() == null || patientRequest.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name of patient cannot be empty");
        }
        if (patientRequest.getEmail() == null || patientRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("Your email cannot be empty");
        }
        if (patientRequest.getGender() == null || patientRequest.getGender().isEmpty()) {
            throw new IllegalArgumentException("Your gender cannot be empty");
        }
        if (patientRequest.getPhoneNumber() == null || patientRequest.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("Your phone number cannot be empty");
        }

        // Kiểm tra ngày tháng năm sinh không được bỏ trống
        String dateOfBirthString = patientRequest.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (dateOfBirthString == null || dateOfBirthString.isEmpty()) {
            throw new IllegalArgumentException("Your date of birth cannot be empty");
        }

        // Kiểm tra phoneNumber có định dạng số và ít nhất 12 ký tự
        if (!patientRequest.getPhoneNumber().matches("^[0-9]{12,}$")) {
            throw new IllegalArgumentException(
                    "Phone number must contain only digits and be at least 12 characters long.");
        }
    }
}
