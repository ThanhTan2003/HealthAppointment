package com.programmingtechie.appointment_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSearchRequest {
    private String medicalRecordsId;
    private String serviceTimeFrameId;
    private String patientsId;
    private String replacementDoctorId;
}
