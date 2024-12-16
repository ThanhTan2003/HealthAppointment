package com.programmingtechie.HIS.minio.properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class MinioConfiguration {

    private final MinioProperties minioProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(minioProperties.getUrl())
                .credentials(minioProperties.getAccessKey(), minioProperties.getSecretKey())
                .build();
    }
}
