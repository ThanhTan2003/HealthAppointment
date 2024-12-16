package com.programmingtechie.HIS;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

import com.programmingtechie.HIS.minio.properties.MinioProperties;

@SpringBootApplication
@EnableFeignClients
@EnableConfigurationProperties(MinioProperties.class)
public class HisApplication {

    public static void main(String[] args) {
        SpringApplication.run(HisApplication.class, args);
    }
}
