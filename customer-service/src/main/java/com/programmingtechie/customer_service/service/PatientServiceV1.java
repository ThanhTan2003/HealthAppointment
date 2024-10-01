package com.programmingtechie.customer_service.service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import org.springframework.stereotype.Service;

import com.programmingtechie.customer_service.dto.request.PatientRequest;
import com.programmingtechie.customer_service.model.Patient;
import com.programmingtechie.customer_service.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientServiceV1 {

    final PatientRepository patientRepository;

    //Thêm hồ sơ khám bệnh cho bệnh nhân
    public void createPatient(PatientRequest patientRequest) {
        validPatient(patientRequest);

        Patient patient = Patient.builder()
                .id(generatePatientID())
                .fullName(patientRequest.getFullName())
                .dateOfBirth(patientRequest.getDateOfBirth())
                .gender(patientRequest.getGender())
                .insuranceId(patientRequest.getInsuranceId())
                .identificationCodeOrPassport(patientRequest.getIdentificationCodeOrPassport())
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
                .build();

        patientRepository.save(patient);
    }

    void validPatient(PatientRequest patientRequest) {
        if (patientRequest.getFullName() == null || patientRequest.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name of patient cannot be empty");
        }
        if (patientRequest.getEmail() == null || patientRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException("patient email cannot be empty");
        }
        if (patientRequest.getGender() == null || patientRequest.getGender().isEmpty()) {
            throw new IllegalArgumentException("patient gender cannot be empty");
        }
        if (patientRequest.getPhoneNumber() == null || patientRequest.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("patient phone number cannot be empty");
        }

        // Kiểm tra ngày tháng năm sinh không được bỏ trống
        String dateOfBirthString = patientRequest.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (dateOfBirthString == null || dateOfBirthString.isEmpty()) {
            throw new IllegalArgumentException("patient date of birth cannot be empty");
        }

        // Kiểm tra phoneNumber có định dạng số và ít nhất 12 ký tự
        if (!patientRequest.getPhoneNumber().matches("^[0-9]{12,}$")) {
            throw new IllegalArgumentException(
                    "Phone number must contain only digits and be at least 12 characters long.");
        }
    }

    // Tạo mã ngẫu nhiên
    public static String generatePatientID() {
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
    //Kết thúc hàm tạo mã ngẫu nhiên

}
