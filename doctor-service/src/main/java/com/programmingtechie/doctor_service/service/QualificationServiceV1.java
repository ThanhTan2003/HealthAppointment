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
import com.programmingtechie.doctor_service.dto.response.QualificationResponse;
import com.programmingtechie.doctor_service.model.Qualification;
import com.programmingtechie.doctor_service.repository.QualificationRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class QualificationServiceV1 {
    final QualificationRepository qualificationRepository;
    final WebClient.Builder webClientBuilder;

    // Lấy tất cả các qualification
    public List<QualificationResponse> getAllQualifications() {
        return qualificationRepository.findAll().stream()
                .map(this::mapToQualificationResponse)
                .collect(Collectors.toList());
    }

    // Lấy tất cả qualifications với phân trang
    public PageResponse<QualificationResponse> getAllQualifications(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Qualification> pageData = qualificationRepository.findAll(pageable);

        return PageResponse.<QualificationResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(this::mapToQualificationResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // Lấy qualification theo abbreviation
    public Optional<QualificationResponse> getQualificationByAbbreviation(String abbreviation) {
        return qualificationRepository.findById(abbreviation).map(this::mapToQualificationResponse);
    }

    // Chuyển đổi từ Qualification sang QualificationResponse
    private QualificationResponse mapToQualificationResponse(Qualification qualification) {
        return QualificationResponse.builder()
                .abbreviation(qualification.getAbbreviation())
                .name(qualification.getName())
                .displayOrder(qualification.getDisplayOrder())
                .build();
    }

    // Chuyển đổi từ DTO QualificationResponse sang Entity Qualification
    private Qualification mapToQualificationEntity(QualificationResponse qualificationResponse) {
        return Qualification.builder()
                .abbreviation(qualificationResponse.getAbbreviation())
                .name(qualificationResponse.getName())
                .displayOrder(qualificationResponse.getDisplayOrder())
                .build();
    }
}
