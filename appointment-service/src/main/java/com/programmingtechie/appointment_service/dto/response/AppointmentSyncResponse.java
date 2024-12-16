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
public class AppointmentSyncResponse {
    private String id;

    private LocalDateTime dateTime;

    private LocalDate date;

    private String status;

    private Integer orderNumber;

    private LocalDateTime lastUpdated;

    private String RoomId;

    private String doctorId;

    private String serviceId;

    private String medicalRecordsId;

    private String patientsId;

    private String customerId;

    private String replacementDoctorId;
}
