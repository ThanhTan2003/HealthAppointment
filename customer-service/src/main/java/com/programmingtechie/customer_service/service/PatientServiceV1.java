package com.programmingtechie.customer_service.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.programmingtechie.customer_service.repository.PatientRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PatientServiceV1 {

    final PatientRepository patientRepository;

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
