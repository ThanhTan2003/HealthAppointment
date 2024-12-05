package com.programmingtechie.appointment_service.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.programmingtechie.appointment_service.dto.response.Medical.ServiceTimeFrameInAppointmentResponse;

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

    private String dateName;

    private String status;

    private Integer orderNumber;

    private LocalDateTime lastUpdated;

    private String serviceTimeFrameId;

    private String timeFrameFullName;

    private String serviceName;

    private String doctorName;

    private String medicalRecordsId;

    private String patientsId;

    private String customerId;

    private String replacementDoctorId;

    private String paymentId;

    private ServiceTimeFrameInAppointmentResponse serviceTimeFrame;
}
