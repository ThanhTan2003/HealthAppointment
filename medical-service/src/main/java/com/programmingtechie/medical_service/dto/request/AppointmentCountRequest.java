package com.programmingtechie.medical_service.dto.request;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentCountRequest {
    private String serviceTimeFrameId;
    private LocalDate date;
}
