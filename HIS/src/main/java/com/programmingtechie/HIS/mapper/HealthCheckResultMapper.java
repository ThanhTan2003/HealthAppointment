package com.programmingtechie.HIS.mapper;

import com.programmingtechie.HIS.dto.response.HealthCheckResultResponse;
import com.programmingtechie.HIS.minio.integration.MinioChannel;
import com.programmingtechie.HIS.model.HealthCheckResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class HealthCheckResultMapper {
    final MinioChannel minioChannel;
    public HealthCheckResultResponse toHealthCheckResultResponse(HealthCheckResult healthCheckResult)
    {
        return HealthCheckResultResponse.builder()
                .id(healthCheckResult.getId())
                .appointmentId(healthCheckResult.getAppointment().getId())
                .fileName(healthCheckResult.getFileName())
                .bucketName(healthCheckResult.getBucketName())
                .name(healthCheckResult.getName())
                .URL(minioChannel.generateFileUrl(healthCheckResult.getFileName(), healthCheckResult.getBucketName()))
                .build();
    }
}
