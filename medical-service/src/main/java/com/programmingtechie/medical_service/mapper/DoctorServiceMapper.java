package com.programmingtechie.medical_service.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.programmingtechie.medical_service.dto.response.Doctor.DoctorResponse;
import com.programmingtechie.medical_service.dto.response.DoctorServiceResponse;
import com.programmingtechie.medical_service.model.DoctorService;
import com.programmingtechie.medical_service.repository.httpClient.DoctorClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class DoctorServiceMapper {
    final DoctorClient doctorClient;

    public DoctorServiceResponse toDoctorServiceResponse(DoctorService doctorService) {
        return DoctorServiceResponse.builder()
                .id(doctorService.getId())
                .doctorId(doctorService.getDoctorId())
                .serviceId(doctorService.getService().getId())
                .isActive(doctorService.getIsActive())
                .service(doctorService.getService())
                .unitPrice(doctorService.getUnitPrice() != null ? doctorService.getUnitPrice() : 0)
                .build();
    }

    public List<DoctorServiceResponse> toListDoctorServiceResponse(List<DoctorService> doctorServices) {
        List<String> doctorIds =
                doctorServices.stream().map(DoctorService::getDoctorId).collect(Collectors.toList());

        List<DoctorResponse> doctorResponses = doctorClient.getByIds(doctorIds);

        return doctorServices.stream()
                .map(doctorService -> {
                    DoctorServiceResponse response = toDoctorServiceResponse(doctorService);

                    DoctorResponse doctorResponse = doctorResponses.stream()
                            .filter(dr -> dr.getId().equals(doctorService.getDoctorId()))
                            .findFirst()
                            .orElse(null);

                    response.setDoctorResponse(doctorResponse);

                    return response;
                })
                .collect(Collectors.toList());
    }
}
