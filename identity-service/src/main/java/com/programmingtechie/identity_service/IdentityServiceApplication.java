package com.programmingtechie.identity_service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class IdentityServiceApplication {

    public static void main(String[] args) {
        // Tải các biến môi trường từ tệp .env
        Dotenv dotenv = Dotenv.load();

        SpringApplication.run(IdentityServiceApplication.class, args);
    }
}
