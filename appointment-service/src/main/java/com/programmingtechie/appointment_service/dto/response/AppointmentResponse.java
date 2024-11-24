package com.programmingtechie.appointment_service.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private String id;

    private LocalDateTime dateTime;

    private LocalDate date;

    private String session;

    private String status;

    private Integer orderNumber;

    private LocalDateTime lastUpdated;

    private String serviceTimeFrameId;

    private String medicalRecordsId;

    private String patientsId;

    private String replacementDoctorId;

    private String billId;
}
