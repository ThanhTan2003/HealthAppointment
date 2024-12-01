package com.programmingtechie.appointment_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentSearchRequest {
    private String medicalRecordsId;
    private String serviceTimeFrameId;
    private String patientsId;
    private String replacementDoctorId;
}
