package com.programmingtechie.medical_service.dto.response;

import com.programmingtechie.medical_service.dto.response.Doctor.DoctorResponse;
import com.programmingtechie.medical_service.dto.response.Doctor.SpecialtyResponse;
import com.programmingtechie.medical_service.model.Service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorServiceResponse {
    private String id;
    private String doctorId;
    private String serviceId;
    private String roomId;
    private Boolean isActive;
    private Service service;
    private SpecialtyResponse specialtyResponse;

    private DoctorResponse doctorResponse;
}
