package com.programmingtechie.HIS.dto.response;

import com.programmingtechie.HIS.model.Doctor;
import com.programmingtechie.HIS.model.HealthCheckResult;
import com.programmingtechie.HIS.model.Room;
import com.programmingtechie.HIS.model.Service;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private String id;

    private LocalDateTime dateTime;

    private LocalDate date;

    private String status;

    private Integer orderNumber;

    private LocalDateTime lastUpdated;

    private Room room;

    private Service service;

    private DoctorResponse doctor;

    private String doctorServiceId;

    private String medicalRecordsId;

    private String patientsId;

    private String customerId;

    private String replacementDoctorId;

    private List<HealthCheckResult> healthCheckResults;
}
