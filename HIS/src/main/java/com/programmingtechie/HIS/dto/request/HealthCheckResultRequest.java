package com.programmingtechie.HIS.dto.request;

import com.programmingtechie.HIS.model.Appointment;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthCheckResultRequest {

    private String appointmentId;

    private String name;

    private MultipartFile file;
}
