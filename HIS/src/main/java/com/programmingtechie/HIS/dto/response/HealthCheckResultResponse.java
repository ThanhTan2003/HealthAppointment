package com.programmingtechie.HIS.dto.response;

import com.programmingtechie.HIS.model.Appointment;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckResultResponse {
    private String id;

    private String appointmentId;

    private String name;

    private String fileName;

    private String bucketName;

    private LocalDateTime lastUpdated;

    private String URL;
}
