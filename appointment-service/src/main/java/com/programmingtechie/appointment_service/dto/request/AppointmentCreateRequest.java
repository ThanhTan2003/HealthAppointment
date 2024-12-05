package com.programmingtechie.appointment_service.dto.request;

import com.programmingtechie.appointment_service.model.Bill;
import com.programmingtechie.appointment_service.model.Payment;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
