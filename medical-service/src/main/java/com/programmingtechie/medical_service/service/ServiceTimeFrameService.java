package com.programmingtechie.medical_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.programmingtechie.medical_service.mapper.ServiceTimeFrameMapper;
import com.programmingtechie.medical_service.model.DoctorService;
import com.programmingtechie.medical_service.model.Room;
import com.programmingtechie.medical_service.repository.DoctorServiceRepository;
import com.programmingtechie.medical_service.repository.RoomRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.programmingtechie.medical_service.dto.request.ServiceTimeFrameRequest;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.dto.response.ServiceTimeFrameResponse;
import com.programmingtechie.medical_service.model.ServiceTimeFrame;
import com.programmingtechie.medical_service.repository.ServiceTimeFrameRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServiceTimeFrameService {
    private final ServiceTimeFrameRepository serviceTimeFrameRepository;
    private final DoctorServiceRepository doctorServiceRepository;

    private final ServiceTimeFrameMapper serviceTimeFrameMapper;


    private final RoomRepository roomRepository;

    public PageResponse<ServiceTimeFrameResponse> getAllServiceTimeFrames(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<ServiceTimeFrame> pageData = serviceTimeFrameRepository.findAll(pageable);

        List<ServiceTimeFrameResponse> serviceTimeFrameResponses = pageData.getContent().stream()
                .map(serviceTimeFrameMapper::toServiceTimeFrameResponse)
                .collect(Collectors.toList());

        return PageResponse.<ServiceTimeFrameResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(serviceTimeFrameResponses)
                .build();
    }

    public ServiceTimeFrameResponse getServiceTimeFrameById(String id) {
        ServiceTimeFrame serviceTimeFrame = serviceTimeFrameRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khung thời gian dịch vụ với id: " + id));
        return serviceTimeFrameMapper.toServiceTimeFrameResponse(serviceTimeFrame);
    }

    public ServiceTimeFrameResponse createServiceTimeFrame(ServiceTimeFrameRequest serviceTimeFrameRequest) {
        // Tìm doctorService theo id, nếu không tìm thấy ném ra ngoại lệ
        DoctorService doctorService = doctorServiceRepository.findById(serviceTimeFrameRequest.getDoctorServiceId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy dịch vụ bác sĩ với id: " + serviceTimeFrameRequest.getDoctorServiceId()));

        // Tìm room theo id, nếu không tìm thấy ném ra ngoại lệ
        Room room = roomRepository.findById(serviceTimeFrameRequest.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng với id: " + serviceTimeFrameRequest.getRoomId()));

        // Tạo đối tượng ServiceTimeFrame mới
        ServiceTimeFrame serviceTimeFrame = ServiceTimeFrame.builder()
                .dayOfWeek(serviceTimeFrameRequest.getDayOfWeek())
                .startTime(serviceTimeFrameRequest.getStartTime())
                .endTime(serviceTimeFrameRequest.getEndTime())
                .maximumQuantity(serviceTimeFrameRequest.getMaximumQuantity())
                .startNumber(serviceTimeFrameRequest.getStartNumber())
                .endNumber(serviceTimeFrameRequest.getEndNumber())
                .doctorService(doctorService)
                .status("Nhận đăng ký")
                .isActive(true)
                .room(room)
                .build();

        serviceTimeFrame = serviceTimeFrameRepository.save(serviceTimeFrame);
        return serviceTimeFrameMapper.toServiceTimeFrameResponse(serviceTimeFrame);
    }

    public ServiceTimeFrameResponse updateServiceTimeFrame(String id, ServiceTimeFrameRequest serviceTimeFrameRequest) {
        ServiceTimeFrame serviceTimeFrame = serviceTimeFrameRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khung thời gian dịch vụ với id: " + id));

        serviceTimeFrame.setDayOfWeek(serviceTimeFrameRequest.getDayOfWeek());
        serviceTimeFrame.setStartTime(serviceTimeFrameRequest.getStartTime());
        serviceTimeFrame.setEndTime(serviceTimeFrameRequest.getEndTime());
        serviceTimeFrame.setMaximumQuantity(serviceTimeFrameRequest.getMaximumQuantity());
        serviceTimeFrame.setStartNumber(serviceTimeFrameRequest.getStartNumber());
        serviceTimeFrame.setEndNumber(serviceTimeFrameRequest.getEndNumber());

        serviceTimeFrame = serviceTimeFrameRepository.save(serviceTimeFrame);
        return serviceTimeFrameMapper.toServiceTimeFrameResponse(serviceTimeFrame);
    }

    public void deleteServiceTimeFrame(String id) {
        ServiceTimeFrame serviceTimeFrame = serviceTimeFrameRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy khung thời gian dịch vụ với id: " + id));
        serviceTimeFrameRepository.delete(serviceTimeFrame);
    }

    public List<ServiceTimeFrameResponse> getServiceTimeFramesByDoctorIdAndDayOfWeek(String doctorId, String dayOfWeek) {
        List<ServiceTimeFrame> serviceTimeFrames = serviceTimeFrameRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);

        return serviceTimeFrames.stream()
                .map(serviceTimeFrameMapper::toServiceTimeFrameResponse)
                .collect(Collectors.toList());
    }

    public PageResponse<String> getDoctorsAllServiceTimeFrames(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<String> doctorIdsPage = serviceTimeFrameRepository.findDistinctDoctorIds(pageable);

        return PageResponse.<String>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(doctorIdsPage.getTotalPages())
                .totalElements(doctorIdsPage.getTotalElements())
                .data(doctorIdsPage.getContent())
                .build();
    }

    public PageResponse<String> getListSpecialtyWithServiceTimeFrames(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<String> doctorIdsPage = serviceTimeFrameRepository.findDistinctSpecialtyIds(pageable);

        return PageResponse.<String>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(doctorIdsPage.getTotalPages())
                .totalElements(doctorIdsPage.getTotalElements())
                .data(doctorIdsPage.getContent())
                .build();
    }

    public List<String> getAvailableDaysForDoctorService(String doctorServiceId) {

        return serviceTimeFrameRepository.findListDayOfWeekByDoctorServiceId(doctorServiceId);
    }
}
