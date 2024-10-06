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

    // Thêm hồ sơ khám bệnh cho bệnh nhân
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

    //Cập nhật hồ sơ khám bệnh
    public void updatePatient(String id, PatientRequest patientRequest) {
        validPatient(patientRequest);
        Optional<Patient> optionalPatient = patientRepository.findById(id);
        boolean isInsuranceIdExists = patientRepository.existsByInsuranceId(patientRequest.getInsuranceId());
        boolean isIdentificationCodeOrPassportExists = patientRepository
                .existsByIdentificationCodeOrPassport(patientRequest.getIdentificationCodeOrPassport());
        if (isInsuranceIdExists) {
            throw new IllegalArgumentException("Mã bảo hiểm y tế không hợp lệ.");
        }
        if (isIdentificationCodeOrPassportExists) {
            throw new IllegalArgumentException("Mã căn cước công dân hoặc hộ chiếu không hợp lệ.");
        }
        if (optionalPatient.isPresent()) {
            Patient patient = Patient.builder()
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
        } else {
            throw new IllegalArgumentException("Patient with ID " + id + " not found");
        }

    }

    // Hàm kiểm tra các thông tin hợp lệ của bệnh nhân
    void validPatient(PatientRequest patientRequest) {
        if (patientRequest.getFullName() == null || patientRequest.getFullName().isEmpty()) {
            throw new IllegalArgumentException("Full name of patient cannot be empty");
        }
        if (patientRequest.getEmail() == null || patientRequest.getEmail().isEmpty()) {
            throw new IllegalArgumentException(" email cannot be empty");
        }
        if (patientRequest.getGender() == null || patientRequest.getGender().isEmpty()) {
            throw new IllegalArgumentException("gender cannot be empty");
        }
        if (patientRequest.getPhoneNumber() == null || patientRequest.getPhoneNumber().isEmpty()) {
            throw new IllegalArgumentException("phone number cannot be empty");
        }
        if (patientRequest.getProvince() == null || patientRequest.getProvince().isEmpty()) {
            throw new IllegalArgumentException("province cannot be empty");
        }
        if (patientRequest.getDistrict() == null || patientRequest.getDistrict().isEmpty()) {
            throw new IllegalArgumentException("distric cannot be empty");
        }
        if (patientRequest.getWard() == null || patientRequest.getWard().isEmpty()) {
            throw new IllegalArgumentException("ward cannot be empty");
        }
        if (patientRequest.getInsuranceId() == null || patientRequest.getInsuranceId().isEmpty()) {
            throw new IllegalArgumentException("insurance id cannot be empty");
        }
        if (patientRequest.getIdentificationCodeOrPassport() == null
                || patientRequest.getIdentificationCodeOrPassport().isEmpty()) {
            throw new IllegalArgumentException("identitycode or passport cannot be empty");
        }
        if (patientRequest.getNation() == null || patientRequest.getNation().isEmpty()) {
            throw new IllegalArgumentException("nation cannot be empty");
        }
        if (patientRequest.getOccupation() == null || patientRequest.getOccupation().isEmpty()) {
            throw new IllegalArgumentException("occupation cannot be empty");
        }

        // Kiểm tra ngày tháng năm sinh không được bỏ trống
        String dateOfBirthString = patientRequest.getDateOfBirth().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        if (dateOfBirthString == null || dateOfBirthString.isEmpty()) {
            throw new IllegalArgumentException("patient date of birth cannot be empty");
        }

        // Kiểm tra phoneNumber có định dạng số và ít nhất 10 ký tự
        if (!patientRequest.getPhoneNumber().matches("^[0-9]{10,}$")) {
            throw new IllegalArgumentException(
                    "Phone number must contain only digits and be at least 12 characters long.");
        }
        // Kiểm tra insurance id có định dạng số và ít nhất 15 ký tự
        if (!patientRequest.getInsuranceId().matches("^[0-9]{15,}$")) {
            throw new IllegalArgumentException(
                    "insurance id must contain only digits and be at least 15 characters long.");
        }
        // Kiểm tra identitycode or passport có định dạng số và ít nhất 12 ký tự
        if (!patientRequest.getIdentificationCodeOrPassport().matches("^[0-9]{12,}$")) {
            throw new IllegalArgumentException(
                    "identitycode or passport must contain only digits and be at least 12 characters long.");
        }
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
    // Kết thúc hàm tạo mã ngẫu nhiên

}
