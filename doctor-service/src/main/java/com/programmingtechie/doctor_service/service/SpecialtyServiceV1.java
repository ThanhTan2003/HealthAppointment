package com.programmingtechie.doctor_service.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.programmingtechie.doctor_service.dto.response.PageResponse;
import com.programmingtechie.doctor_service.dto.response.SpecialtyResponse;
import com.programmingtechie.doctor_service.mapper.SpecialtyMapper;
import com.programmingtechie.doctor_service.model.Specialty;
import com.programmingtechie.doctor_service.repository.SpecialtyRepository;
import com.programmingtechie.doctor_service.repository.httpClient.MedicalClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SpecialtyServiceV1 {
    final SpecialtyRepository specialtyRepository;
    final WebClient.Builder webClientBuilder;

    final SpecialtyMapper specialtyMapper;

    final MedicalClient medicalClient;

    // Lấy tất cả specialties với phân trang
    public PageResponse<SpecialtyResponse> getAllSpecialties(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Specialty> pageData = specialtyRepository.findAll(pageable);

        return PageResponse.<SpecialtyResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(specialtyMapper::toSpecialtyResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // Lấy specialty theo id
    public Optional<SpecialtyResponse> getSpecialtyById(String id) {
        return specialtyRepository.findById(id).map(specialtyMapper::toSpecialtyResponse);
    }

    public PageResponse<SpecialtyResponse> searchSpecialties(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Specialty> pageData = specialtyRepository.searchSpecialties(keyword, pageable);

        return PageResponse.<SpecialtyResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(specialtyMapper::toSpecialtyResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public List<SpecialtyResponse> getSpecialtiesByIds(List<String> specialtyIds) {
        List<Specialty> specialties = specialtyRepository.findByIdIn(specialtyIds);
        if (specialtyIds.isEmpty()) {
            log.info("Rỗng ...");
        }
        log.info(specialties.toString());
        return specialties.stream().map(specialtyMapper::toSpecialtyResponse).collect(Collectors.toList());
    }

    public PageResponse<SpecialtyResponse> getAllSpecialtiesByCustomer(int page, int size) {
        // Gọi Medical Service
        PageResponse<String> response = medicalClient.getListSpecialtyWithServiceTimeFrames(page, size);

        List<String> specialtyIdsWithService = response.getData();

        // Tìm danh sách bác sĩ dựa trên danh sách doctorId đã có
        List<Specialty> specialtyList = specialtyRepository.findByIdIn(specialtyIdsWithService);

        List<SpecialtyResponse> specialtyResponses =
                specialtyList.stream().map(specialtyMapper::toSpecialtyResponse).collect(Collectors.toList());

        return PageResponse.<SpecialtyResponse>builder()
                .currentPage(response.getCurrentPage())
                .pageSize(response.getPageSize())
                .totalPages(response.getTotalPages())
                .totalElements(response.getTotalElements())
                .data(specialtyResponses)
                .build();
    }
}
