package com.programmingtechie.identity_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class IdentityServiceApplication {

    public static void main(String[] args) {
        // Tải các biến môi trường từ tệp .env
        // Dotenv dotenv = Dotenv.load();

        SpringApplication.run(IdentityServiceApplication.class, args);
    }
}
