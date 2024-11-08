package com.programmingtechie.medical_service.mapper;

import org.springframework.stereotype.Component;

import com.programmingtechie.medical_service.dto.response.DoctorServiceResponse;
import com.programmingtechie.medical_service.model.DoctorService;

@Component
public class DoctorServiceMapper {
    public DoctorServiceResponse toDoctorServiceResponse(DoctorService doctorService) {
        return DoctorServiceResponse.builder()
                .id(doctorService.getId())
                .doctorId(doctorService.getDoctorId())
                .serviceId(doctorService.getService().getId())
                .roomId(
                        doctorService.getRoom() != null
                                ? doctorService.getRoom().getId()
                                : null)
                .status(doctorService.getStatus())
                .build();
    }
}