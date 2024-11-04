package com.programmingtechie.medical_service.mapper;


import com.programmingtechie.medical_service.dto.response.ServiceResponse;
import com.programmingtechie.medical_service.model.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ServiceMapper {
    private final ServiceTypeMapper serviceTypeMapper;

    public ServiceResponse toServiceResponse(Service service)
    {
        return ServiceResponse.builder()
            .id(service.getId())
            .name(service.getName())
            .unitPrice(service.getUnitPrice())
            .description(service.getDescription())
            .status(service.getStatus())
            .specialtyId((service.getSpecialtyId()))
            .serviceTypeId(service.getServiceType().getId())
            .serviceType(serviceTypeMapper.toServiceTypeResponse(service.getServiceType()))
            .build();
    }
}
