package com.programmingtechie.medical_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

import io.github.cdimascio.dotenv.Dotenv;

@SpringBootApplication
@EnableFeignClients
public class MedicalServiceApplication {

    // Tải các biến môi trường từ tệp .env
    static Dotenv dotenv = Dotenv.load();

    public static void main(String[] args) {
        setProperty();

        SpringApplication.run(MedicalServiceApplication.class, args);
    }

    // Đặt biến môi trường cho hệ thống để Spring Boot có thể nhận diện
    static void setProperty() {
        System.setProperty("MEDICAL_SERVICE_DB_URL", dotenv.get("MEDICAL_SERVICE_DB_URL"));
        System.setProperty("MEDICAL_SERVICE_DB_USER", dotenv.get("MEDICAL_SERVICE_DB_USERNAME"));
        System.setProperty("MEDICAL_SERVICE_DB_PASS", dotenv.get("MEDICAL_SERVICE_DB_PASSWORD"));
    }
}
