package com.programmingtechie.HIS.dto.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckResultRequest {

    private String appointmentId;

    private String name;

    private MultipartFile file;
}
