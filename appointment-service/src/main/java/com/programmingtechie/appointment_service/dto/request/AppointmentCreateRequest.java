package com.programmingtechie.appointment_service.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.programmingtechie.appointment_service.model.Payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentCreateRequest {

    private LocalDateTime dateTime;

    private LocalDate date;

    private Integer orderNumber;

    private String serviceTimeFrameId;

    private String patientsId;

    private String customerId;

    private String replacementDoctorId;

    private Payment payment;
}
