package com.programmingtechie.medical_service.mapper;


import com.programmingtechie.medical_service.dto.response.ServiceResponse;
import com.programmingtechie.medical_service.dto.response.SpecialtyResponse;
import com.programmingtechie.medical_service.model.Service;
import com.programmingtechie.medical_service.repository.httpClient.DoctorClient;
import jakarta.servlet.Servlet;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@RequiredArgsConstructor
@Slf4j
public class ServiceMapper {
    private final ServiceTypeMapper serviceTypeMapper;
    private  final DoctorClient doctorClient;

    public ServiceResponse toServiceResponse(Service service)
    {
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

        try{
            ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

            SpecialtyResponse specialtyResponse = doctorClient.getSpecialtyById(serviceResponse.getSpecialtyId());
            serviceResponse.setSpecialtyResponse(specialtyResponse);
        }
        catch (Exception e)
        {
            log.error("Lỗi kết nối đến Doctor service: " + e.toString());
        }
        return  serviceResponse;
    }
}
