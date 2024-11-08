package com.programmingtechie.medical_service.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;

import com.programmingtechie.medical_service.dto.response.Doctor.SpecialtyResponse;
import com.programmingtechie.medical_service.dto.response.ServiceResponse;
import com.programmingtechie.medical_service.model.Service;
import com.programmingtechie.medical_service.repository.httpClient.DoctorClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceMapper {
    private final ServiceTypeMapper serviceTypeMapper;
    private final DoctorClient doctorClient;

    public ServiceResponse toServiceResponse(Service service) {
        ServiceResponse serviceResponse = ServiceResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .unitPrice(service.getUnitPrice())
                .description(service.getDescription())
                .status(service.getStatus())
                .specialtyId((service.getSpecialtyId()))
                .serviceTypeId(service.getServiceType().getId())
                .serviceType(serviceTypeMapper.toServiceTypeResponse(service.getServiceType()))
                .build();

        try {
            SpecialtyResponse specialtyResponse = doctorClient.getSpecialtyById(serviceResponse.getSpecialtyId());
            serviceResponse.setSpecialtyResponse(specialtyResponse);
        } catch (Exception e) {
            log.error("Lỗi kết nối đến Doctor service: " + e.toString());
        }
        return serviceResponse;
    }

    public List<ServiceResponse> toListServiceResponse(List<Service> services) {
        List<ServiceResponse> serviceResponses = new ArrayList<>();

        List<String> specialtyIds = new ArrayList<>();
        for (Service service : services) {
            if (service.getSpecialtyId() != null) specialtyIds.add(service.getSpecialtyId());
        }

        List<SpecialtyResponse> specialtyResponses = doctorClient.getSpecialtiesByIds(specialtyIds);
        for (Service service : services) {
            ServiceResponse serviceResponse = ServiceResponse.builder()
                    .id(service.getId())
                    .name(service.getName())
                    .unitPrice(service.getUnitPrice())
                    .description(service.getDescription())
                    .status(service.getStatus())
                    .specialtyId((service.getSpecialtyId()))
                    .serviceTypeId(service.getServiceType().getId())
                    .serviceType(serviceTypeMapper.toServiceTypeResponse(service.getServiceType()))
                    .build();

            for (SpecialtyResponse specialtyResponse : specialtyResponses)
                if (Objects.equals(specialtyResponse.getSpecialtyId(), serviceResponse.getSpecialtyId())) {
                    serviceResponse.setSpecialtyResponse(specialtyResponse);
                    break;
                }
            serviceResponses.add(serviceResponse);
        }
        return serviceResponses;
    }
}
