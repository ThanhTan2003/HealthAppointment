package com.programmingtechie.medical_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.programmingtechie.medical_service.dto.request.DoctorServiceRequest;
import com.programmingtechie.medical_service.dto.response.Doctor.SpecialtyResponse;
import com.programmingtechie.medical_service.dto.response.DoctorServiceResponse;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.mapper.DoctorServiceMapper;
import com.programmingtechie.medical_service.model.DoctorService;
import com.programmingtechie.medical_service.repository.DoctorServiceRepository;
import com.programmingtechie.medical_service.repository.ServiceRepository;
import com.programmingtechie.medical_service.repository.httpClient.DoctorClient;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DoctorServiceService {
    private final DoctorServiceRepository doctorServiceRepository;
    private final ServiceRepository serviceRepository;

    private final ServiceService serviceService;

    private final DoctorClient doctorClient;

    private final DoctorServiceMapper doctorServiceMapper;

    public PageResponse<DoctorServiceResponse> getAllDoctorServices(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DoctorService> pageData = doctorServiceRepository.findAll(pageable);

        List<DoctorServiceResponse> doctorServiceResponses = pageData.getContent().stream()
                .map(doctorServiceMapper::toDoctorServiceResponse)
                .collect(Collectors.toList());

        return PageResponse.<DoctorServiceResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(doctorServiceResponses)
                .build();
    }

    public DoctorServiceResponse getDoctorServiceById(String id) {
        DoctorService doctorService = doctorServiceRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ bác sĩ với id: " + id));
        return doctorServiceMapper.toDoctorServiceResponse(doctorService);
    }

    public DoctorServiceResponse createDoctorService(DoctorServiceRequest doctorServiceRequest) {
        // Kiểm tra xem sự kết hợp của DoctorId và ServiceId đã tồn tại trong bảng DoctorService chưa
        if (doctorServiceRepository.existsByDoctorIdAndServiceId(
                doctorServiceRequest.getDoctorId(), doctorServiceRequest.getServiceId())) {
            throw new IllegalArgumentException("Dịch vụ bác sĩ đã tồn tại!");
        }

        if (doctorServiceRequest.getServiceId().isEmpty())
            throw new IllegalArgumentException("Chưa có thông tin dịch vụ!");

        com.programmingtechie.medical_service.model.Service service = serviceRepository
                .findById(doctorServiceRequest.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy dịch vụ với id: " + doctorServiceRequest.getServiceId()));

        DoctorService doctorService = DoctorService.builder()
                .doctorId(doctorServiceRequest.getDoctorId())
                .service(service)
                .isActive(doctorServiceRequest.getIsActive())
                .build();

        doctorService = doctorServiceRepository.save(doctorService);
        return doctorServiceMapper.toDoctorServiceResponse(doctorService);
    }

    public DoctorServiceResponse updateDoctorService(String id, DoctorServiceRequest doctorServiceRequest) {
        DoctorService doctorService = doctorServiceRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ bác sĩ với id: " + id));

        doctorService.setDoctorId(doctorServiceRequest.getDoctorId());
        doctorService.setIsActive(doctorServiceRequest.getIsActive());

        doctorService = doctorServiceRepository.save(doctorService);
        return doctorServiceMapper.toDoctorServiceResponse(doctorService);
    }

    public void deleteDoctorService(String id) {
        DoctorService doctorService = doctorServiceRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy dịch vụ bác sĩ với id: " + id));
        doctorServiceRepository.delete(doctorService);
    }

    // Lay danh sach dịch vu cua bac si theo doctorId
    public PageResponse<DoctorServiceResponse> getDoctorServicesByDoctorId(String doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<DoctorService> doctorServices = doctorServiceRepository.findByDoctorId(doctorId, pageable);

        List<DoctorServiceResponse> doctorServiceResponses = doctorServices.getContent().stream()
                .map(doctorServiceMapper::toDoctorServiceResponse)
                .collect(Collectors.toList());

        // Lấy danh sách serviceIds
        List<String> serviceIds = doctorServiceResponses.stream()
                .map(DoctorServiceResponse::getServiceId)
                .collect(Collectors.toList());

        // Lấy danh sách service từ serviceRepository
        List<com.programmingtechie.medical_service.model.Service> services =
                serviceRepository.getServicesByIdIn(serviceIds);
        Map<String, com.programmingtechie.medical_service.model.Service> serviceMap = services.stream()
                .collect(Collectors.toMap(
                        com.programmingtechie.medical_service.model.Service::getId, service -> service));

        // Gán dịch vụ vào DoctorServiceResponse và lấy specialtyIds
        List<String> specialtyIds = new ArrayList<>();
        for (DoctorServiceResponse doctorServiceResponse : doctorServiceResponses) {
            doctorServiceResponse.setService(serviceMap.get(doctorServiceResponse.getServiceId()));
            if (doctorServiceResponse.getService() != null) {
                String specialtyId = doctorServiceResponse.getService().getSpecialtyId();
                if (specialtyId != null && !specialtyId.isEmpty()) {
                    specialtyIds.add(specialtyId);
                }
            }
        }

        // Lấy danh sách SpecialtyResponse từ DoctorClient
        List<SpecialtyResponse> specialtyResponses = doctorClient.getSpecialtiesByIds(specialtyIds);
        Map<String, SpecialtyResponse> specialtyResponseMap = specialtyResponses.stream()
                .collect(Collectors.toMap(SpecialtyResponse::getSpecialtyId, specialtyResponse -> specialtyResponse));

        // Gán SpecialtyResponse vào DoctorServiceResponse
        for (DoctorServiceResponse doctorServiceResponse : doctorServiceResponses) {
            if (doctorServiceResponse.getService() != null) {
                doctorServiceResponse.setSpecialtyResponse(specialtyResponseMap.get(
                        doctorServiceResponse.getService().getSpecialtyId()));
            }
        }

        return PageResponse.<DoctorServiceResponse>builder()
                .totalPages(doctorServices.getTotalPages())
                .currentPage(doctorServices.getNumber() + 1) // Trả lại số trang bắt đầu từ 1 cho người dùng
                .pageSize(doctorServices.getSize())
                .totalElements(doctorServices.getTotalElements())
                .data(doctorServiceResponses)
                .build();
    }

    public PageResponse<DoctorServiceResponse> getDoctorServicesByServiceId(String serviceId, int page, int size) {
        // Tạo Pageable để phân trang
        Pageable pageable = PageRequest.of(page - 1, size);

        // Lấy danh sách DoctorService theo serviceId và phân trang
        Page<DoctorService> doctorServices =
                doctorServiceRepository.findByDoctorServiceExistsInServiceTimeFrameByServiceId(serviceId, pageable);

        // Chuyển đổi danh sách DoctorService thành DoctorServiceResponse thông qua mapper
        List<DoctorServiceResponse> doctorServiceResponses =
                doctorServiceMapper.toListDoctorServiceResponse(doctorServices.getContent());

        // Lấy danh sách doctorIds từ DoctorServiceResponse
        List<String> doctorIds = doctorServiceResponses.stream()
                .map(DoctorServiceResponse::getDoctorId)
                .toList();

        // Trả về kết quả phân trang
        return PageResponse.<DoctorServiceResponse>builder()
                .totalPages(doctorServices.getTotalPages())
                .currentPage(doctorServices.getNumber() + 1) // Trả lại số trang bắt đầu từ 1 cho người dùng
                .pageSize(doctorServices.getSize())
                .totalElements(doctorServices.getTotalElements())
                .data(doctorServiceResponses)
                .build();
    }

    public PageResponse<DoctorServiceResponse> getDoctorServicesByDoctorIdPublic(String doctorId, int page, int size) {
        // Tạo Pageable để phân trang
        Pageable pageable = PageRequest.of(page - 1, size);

        // Lấy danh sách DoctorService theo serviceId và phân trang
        Page<DoctorService> doctorServices =
                doctorServiceRepository.findByDoctorServiceExistsInServiceTimeFrame(doctorId, pageable);

        // Chuyển đổi danh sách DoctorService thành DoctorServiceResponse thông qua mapper
        List<DoctorServiceResponse> doctorServiceResponses =
                doctorServiceMapper.toListDoctorServiceResponse(doctorServices.getContent());

        // Lấy danh sách doctorIds từ DoctorServiceResponse
        List<String> doctorIds = doctorServiceResponses.stream()
                .map(DoctorServiceResponse::getDoctorId)
                .toList();

        // Trả về kết quả phân trang
        return PageResponse.<DoctorServiceResponse>builder()
                .totalPages(doctorServices.getTotalPages())
                .currentPage(doctorServices.getNumber() + 1) // Trả lại số trang bắt đầu từ 1 cho người dùng
                .pageSize(doctorServices.getSize())
                .totalElements(doctorServices.getTotalElements())
                .data(doctorServiceResponses)
                .build();
    }
}
