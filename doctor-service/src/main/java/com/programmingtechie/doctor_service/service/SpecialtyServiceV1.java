package com.programmingtechie.doctor_service.service;

import com.programmingtechie.doctor_service.dto.response.PageResponse;
import com.programmingtechie.doctor_service.dto.response.SpecialtyResponse;
import com.programmingtechie.doctor_service.model.Specialty;
import com.programmingtechie.doctor_service.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SpecialtyServiceV1 {
    final SpecialtyRepository specialtyRepository;
    final WebClient.Builder webClientBuilder;

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
                        .map(this::mapToSpecialtyResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // Lấy specialty theo id
    public Optional<SpecialtyResponse> getSpecialtyById(String id) {
        return specialtyRepository.findById(id)
                .map(this::mapToSpecialtyResponse);
    }

    // Chuyển đổi từ Specialty sang SpecialtyResponse
    private SpecialtyResponse mapToSpecialtyResponse(Specialty specialty) {
        return SpecialtyResponse.builder()
                .specialtyId(specialty.getId())
                .specialtyName(specialty.getName())
                .description(specialty.getDescription())
                .build();
    }

    // Chuyển đổi từ DTO SpecialtyResponse sang Entity Specialty
    private Specialty mapToSpecialtyEntity(SpecialtyResponse specialtyResponse) {
        return Specialty.builder()
                .id(specialtyResponse.getSpecialtyId())
                .name(specialtyResponse.getSpecialtyName())
                .description(specialtyResponse.getDescription())
                .build();
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
                        .map(this::mapToSpecialtyResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
