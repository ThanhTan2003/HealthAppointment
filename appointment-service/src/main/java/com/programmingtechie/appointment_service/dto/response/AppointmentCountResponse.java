package com.programmingtechie.appointment_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCountResponse {
    private String serviceTimeFrameId;
    private LocalDate date;
    private Long total;
}
