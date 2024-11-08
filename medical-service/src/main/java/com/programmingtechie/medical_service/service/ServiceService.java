package com.programmingtechie.medical_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.programmingtechie.medical_service.dto.request.ServiceRequest;
import com.programmingtechie.medical_service.dto.response.Doctor.DoctorResponse;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.dto.response.ServiceResponse;
import com.programmingtechie.medical_service.mapper.ServiceMapper;
import com.programmingtechie.medical_service.model.ServiceType;
import com.programmingtechie.medical_service.repository.ServiceRepository;
import com.programmingtechie.medical_service.repository.ServiceTypeRepository;
import com.programmingtechie.medical_service.repository.httpClient.DoctorClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class ServiceService {
    private final ServiceRepository serviceRepository;
    private final ServiceTypeRepository serviceTypeRepository;

    private final DoctorClient doctorClient;

    private final ServiceMapper serviceMapper;

    public PageResponse<ServiceResponse> getAllServices(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<com.programmingtechie.medical_service.model.Service> pageData = serviceRepository.getAllService(pageable);

        List<ServiceResponse> serviceResponses = pageData.getContent().stream()
                .map(serviceMapper::toServiceResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceResponses)
                .build();
    }

    public ServiceResponse getServiceById(String id) {
        com.programmingtechie.medical_service.model.Service service = serviceRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + id));
        return serviceMapper.toServiceResponse(service);
    }

    public ServiceResponse createService(ServiceRequest serviceRequest) {
        ServiceType serviceType = serviceTypeRepository
                .findById(serviceRequest.getServiceTypeId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Service Type not found with id: " + serviceRequest.getServiceTypeId()));

        com.programmingtechie.medical_service.model.Service service =
                com.programmingtechie.medical_service.model.Service.builder()
                        .name(serviceRequest.getName())
                        .unitPrice(serviceRequest.getUnitPrice())
                        .description(serviceRequest.getDescription())
                        .status(serviceRequest.getStatus())
                        .serviceType(serviceType)
                        .build();

        service = serviceRepository.save(service);
        return serviceMapper.toServiceResponse(service);
    }

    public ServiceResponse updateService(String id, ServiceRequest serviceRequest) {
        com.programmingtechie.medical_service.model.Service service = serviceRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service not found with id: " + id));

        ServiceType serviceType = serviceTypeRepository
                .findById(serviceRequest.getServiceTypeId())
                .orElseThrow(() ->
                        new RuntimeException("Service Type not found with id: " + serviceRequest.getServiceTypeId()));

        service.setName(serviceRequest.getName());
        service.setUnitPrice(serviceRequest.getUnitPrice());
        service.setDescription(serviceRequest.getDescription());
        service.setStatus(serviceRequest.getStatus());
        service.setServiceType(serviceType);

        service = serviceRepository.save(service);
        return serviceMapper.toServiceResponse(service);
    }

    public void deleteService(String id) {
        com.programmingtechie.medical_service.model.Service service = serviceRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Service not found with id: " + id));
        serviceRepository.delete(service);
    }

    public PageResponse<ServiceResponse> searchServices(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<com.programmingtechie.medical_service.model.Service> pageData =
                serviceRepository.searchServices(keyword, pageable);

        List<ServiceResponse> serviceResponses = pageData.getContent().stream()
                .map(serviceMapper::toServiceResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceResponses)
                .build();
    }

    public PageResponse<ServiceResponse> getServicesByServiceTypeId(String serviceTypeId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<com.programmingtechie.medical_service.model.Service> pageData =
                serviceRepository.findByServiceTypeId(serviceTypeId, pageable);

        List<ServiceResponse> serviceResponses = pageData.getContent().stream()
                .map(serviceMapper::toServiceResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceResponses)
                .build();
    }

    public PageResponse<ServiceResponse> getServicesBySpecialtyId(String specialtyId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<com.programmingtechie.medical_service.model.Service> pageData =
                serviceRepository.findBySpecialtyId(specialtyId, pageable);

        List<ServiceResponse> serviceResponses = pageData.getContent().stream()
                .map(serviceMapper::toServiceResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceResponses)
                .build();
    }

    public PageResponse<ServiceResponse> getServicesWithSpecialtyIdNotNull(
            String doctorId, String specialtyId, String keyword, int page, int size) {
        DoctorResponse doctorResponse = doctorClient.getById(doctorId);

        List<String> specialties = new ArrayList<>();
        for (int i = 0; i < doctorResponse.getSpecialties().size(); i++) {
            specialties.add(doctorResponse.getSpecialties().get(i).getSpecialtyId());
        }

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<com.programmingtechie.medical_service.model.Service> pageData;

        if (specialtyId == null || specialtyId.isEmpty()) {
            // Tìm kiếm theo danh sách specialties của bác sĩ và keyword
            pageData = serviceRepository.findServicesBySpecialtyIdsAndNotAssignedToDoctorAndKeyword(
                    specialties, doctorId, keyword, pageable);
        } else {
            // Tìm kiếm theo specialtyId và keyword
            pageData = serviceRepository.findServicesBySpecialtyIdAndNotAssignedToDoctorAndKeyword(
                    specialtyId, doctorId, keyword, pageable);
        }

        List<ServiceResponse> serviceResponses = pageData.getContent().stream()
                .map(serviceMapper::toServiceResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceResponses)
                .build();
    }

    public PageResponse<ServiceResponse> getServicesWithServiceTypeNotNull(String doctorId, String serviceTypeId, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<com.programmingtechie.medical_service.model.Service> pageData;
        log.info("serviceTypeId: " + serviceTypeId);
        if(serviceTypeId == null || serviceTypeId.isEmpty())
            pageData = serviceRepository.findServicesWithServiceTypeNotNullAndNotAssignedToDoctor(doctorId, keyword, pageable);
        else
        {
            log.info("Chay.........");
            pageData = serviceRepository.findServicesWithServiceTypeIdNotNullAndNotAssignedToDoctor(doctorId, serviceTypeId, keyword, pageable);
        }

        List<ServiceResponse> serviceResponses = pageData.getContent().stream()
                .map(serviceMapper::toServiceResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceResponses)
                .build();
    }

    public List<ServiceResponse> getServicesByIds(List<String> specialtyIds) {
        List<com.programmingtechie.medical_service.model.Service> services =
                serviceRepository.findAllById(specialtyIds);
        return services.stream().map(serviceMapper::toServiceResponse).collect(Collectors.toList());
    }
}

// private ServiceResponse mapToServiceResponse(com.programmingtechie.medical_service.model.Service service) {
//    return ServiceResponse.builder()
//            .id(service.getId())
//            .name(service.getName())
//            .unitPrice(service.getUnitPrice())
//            .description(service.getDescription())
//            .status(service.getStatus())
//            .specialtyId((service.getSpecialtyId()))
//            .serviceTypeId(service.getServiceType().getId())
//            .serviceType(service.getServiceType())
//            .build();
// }
