package com.programmingtechie.medical_service.service;

import java.util.List;
import java.util.stream.Collectors;

import com.programmingtechie.medical_service.mapper.ServiceTypeMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.programmingtechie.medical_service.dto.request.ServiceTypeRequest;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.dto.response.ServiceTypeResponse;
import com.programmingtechie.medical_service.model.ServiceType;
import com.programmingtechie.medical_service.repository.ServiceTypeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceTypeService {
    private final ServiceTypeRepository serviceTypeRepository;

    private final ServiceTypeMapper serviceTypeMapper;

    public PageResponse<ServiceTypeResponse> getAllServiceTypes(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ServiceType> pageData = serviceTypeRepository.findDistinctServiceTypeIds(pageable);

        List<ServiceTypeResponse> serviceTypeResponses = pageData.getContent().stream()
                .map(serviceTypeMapper::toServiceTypeResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceTypeResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceTypeResponses)
                .build();
    }

    public ServiceTypeResponse getServiceTypeById(String id) {
        ServiceType serviceType = serviceTypeRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại dịch vụ với id: " + id));
        return serviceTypeMapper.toServiceTypeResponse(serviceType);
    }

    public ServiceTypeResponse createServiceType(ServiceTypeRequest serviceTypeRequest) {
        ServiceType serviceType = ServiceType.builder()
                .id((serviceTypeRequest.getId()))
                .name(serviceTypeRequest.getName())
                .description(serviceTypeRequest.getDescription())
                .build();

        serviceType = serviceTypeRepository.save(serviceType);
        return serviceTypeMapper.toServiceTypeResponse(serviceType);
    }

    public ServiceTypeResponse updateServiceType(String id, ServiceTypeRequest serviceTypeRequest) {
        ServiceType serviceType = serviceTypeRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại dịch vụ với id: " + id));

        serviceType.setName(serviceTypeRequest.getName());
        serviceType.setDescription(serviceTypeRequest.getDescription());

        serviceType = serviceTypeRepository.save(serviceType);
        return serviceTypeMapper.toServiceTypeResponse(serviceType);
    }

    public void deleteServiceType(String id) {
        ServiceType serviceType = serviceTypeRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy loại dịch vụ với id: " + id));
        serviceTypeRepository.delete(serviceType);
    }

    public PageResponse<ServiceTypeResponse> searchServiceTypes(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ServiceType> pageData = serviceTypeRepository.searchServiceTypes(keyword, pageable);

        List<ServiceTypeResponse> serviceTypeResponses = pageData.getContent().stream()
                .map(serviceTypeMapper::toServiceTypeResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceTypeResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceTypeResponses)
                .build();
    }
}
