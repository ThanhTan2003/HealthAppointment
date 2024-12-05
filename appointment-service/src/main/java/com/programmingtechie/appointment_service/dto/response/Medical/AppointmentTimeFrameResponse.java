package com.programmingtechie.appointment_service.dto.response.Medical;

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
public class AppointmentTimeFrameResponse {
    private String id;
    private LocalDateTime dateTime;
    private LocalDate date;
    private String status;
    private Integer orderNumber;
    private LocalDateTime lastUpdated;
    private String medicalRecordsId;
    private String patientsId;
    private String customerId;
    private String replacementDoctorId;
    private String paymentId;
    private ServiceTimeFrameResponse serviceTimeFrameResponse;
}
