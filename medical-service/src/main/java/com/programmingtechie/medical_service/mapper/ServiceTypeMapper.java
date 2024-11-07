package com.programmingtechie.medical_service.mapper;

import org.springframework.stereotype.Component;

import com.programmingtechie.medical_service.dto.response.ServiceTypeResponse;
import com.programmingtechie.medical_service.model.ServiceType;

@Component
public class ServiceTypeMapper {
    public ServiceTypeResponse toServiceTypeResponse(ServiceType serviceType) {
        return ServiceTypeResponse.builder()
                .id(serviceType.getId())
                .name(serviceType.getName())
                .description(serviceType.getDescription())
                .build();
    }
}
