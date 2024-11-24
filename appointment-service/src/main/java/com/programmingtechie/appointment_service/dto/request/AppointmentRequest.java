package com.programmingtechie.appointment_service.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {
    private LocalDate date;

    private String session;

    private String serviceTimeFrameId;

    private String patientsId;
}
