package com.programmingtechie.medical_service.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCountResponse {
    private String serviceTimeFrameId;
    private LocalDate date;
    private Long total;
}
