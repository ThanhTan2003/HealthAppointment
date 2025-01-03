package com.programmingtechie.medical_service.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.programmingtechie.medical_service.model.ServiceTimeFrame;
import com.programmingtechie.medical_service.repository.ServiceTimeFrameRepository;
import com.programmingtechie.medical_service.repository.ServiceTypeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final ServiceTimeFrameRepository serviceTimeFrameRepository;

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

    @Transactional
    public DoctorServiceResponse createDoctorService(DoctorServiceRequest doctorServiceRequest) {

        com.programmingtechie.medical_service.model.Service service = serviceRepository
                .findById(doctorServiceRequest.getServiceId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Không tìm thấy dịch vụ với id: " + doctorServiceRequest.getServiceId()));

        // Kiểm tra xem sự kết hợp của DoctorId và ServiceId đã tồn tại trong bảng DoctorService chưa
        DoctorService existingDoctorService = doctorServiceRepository.findByDoctorIdAndServiceId(
                doctorServiceRequest.getDoctorId(), doctorServiceRequest.getServiceId());

        if (existingDoctorService != null) {
            if (existingDoctorService.getIsActive()) {
                // Nếu đã tồn tại và isActive = true, ném lỗi
                throw new IllegalArgumentException("Dịch vụ bác sĩ đã tồn tại!");
            } else {
                // Nếu đã tồn tại và isActive = false, chỉ cần set lại isActive = true
                existingDoctorService.setIsActive(true);
                existingDoctorService.setUnitPrice(service.getUnitPrice());
                existingDoctorService.setLastUpdated(LocalDateTime.now());

                // Lưu lại và trả về thông tin cập nhật
                existingDoctorService = doctorServiceRepository.save(existingDoctorService);
                return doctorServiceMapper.toDoctorServiceResponse(existingDoctorService);
            }
        }

        // Nếu không tồn tại, tạo mới DoctorService
        if (doctorServiceRequest.getServiceId().isEmpty()) {
            throw new IllegalArgumentException("Chưa có thông tin dịch vụ!");
        }

        DoctorService doctorService = DoctorService.builder()
                .doctorId(doctorServiceRequest.getDoctorId())
                .service(service)
                .isActive(true)
                .unitPrice(service.getUnitPrice())
                .build();

        doctorService = doctorServiceRepository.save(doctorService);
        return doctorServiceMapper.toDoctorServiceResponse(doctorService);
    }

    @Transactional
    public DoctorServiceResponse updateDoctorService(String id, Map<String, Object> updates) {
        // Tìm kiếm DoctorService theo id
        DoctorService doctorService = doctorServiceRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ bác sĩ với id: " + id));

        // Duyệt qua các cặp key-value trong updates để cập nhật các trường trong doctorService
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            // Cập nhật các trường tương ứng
            switch (key) {
                case "doctorId":
                    doctorService.setDoctorId((String) value);
                    break;
                case "isActive":
                    doctorService.setIsActive((Boolean) value);
                    break;
                case "unitPrice":
                    // Xử lý để ép kiểu về Double nếu giá trị là String hoặc Integer
                    if (value instanceof String) {
                        try {
                            doctorService.setUnitPrice(
                                    Double.valueOf((String) value)); // Chuyển đổi từ String thành Double
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("unitPrice phải là số hợp lệ");
                        }
                    } else if (value instanceof Integer) {
                        doctorService.setUnitPrice(((Integer) value).doubleValue()); // Chuyển Integer thành Double
                    } else if (value instanceof Double) {
                        doctorService.setUnitPrice((Double) value); // Nếu đã là Double, giữ nguyên
                    } else {
                        throw new IllegalArgumentException("unitPrice phải là kiểu số hợp lệ");
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Trường cập nhật không hợp lệ: " + key);
            }
        }

        // Lưu lại DoctorService đã được cập nhật
        doctorService = doctorServiceRepository.save(doctorService);

        // Chuyển đổi thành DoctorServiceResponse và trả về
        return doctorServiceMapper.toDoctorServiceResponse(doctorService);
    }

    public void deleteDoctorService(String id) {
        DoctorService doctorService = doctorServiceRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ bác sĩ với id: " + id));

        List<ServiceTimeFrame> serviceTimeFrames = serviceTimeFrameRepository.getByDoctorServiceId(id);

        if (serviceTimeFrames.size() > 0)
            throw new IllegalArgumentException("Đã có lịch khám đang hoạt động. Vui lòng kiểm tra lại!");

        doctorService.setIsActive(false);
        doctorServiceRepository.save(doctorService);
        //doctorServiceRepository.delete(doctorService);
    }

    // Lay danh sach dịch vu cua bac si theo doctorId
    public PageResponse<DoctorServiceResponse> getDoctorServicesByDoctorId(String doctorId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());
        Page<DoctorService> doctorServices = doctorServiceRepository.findByDoctorIdAndIsActiveTrue(doctorId, pageable);

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
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("id").ascending());

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
