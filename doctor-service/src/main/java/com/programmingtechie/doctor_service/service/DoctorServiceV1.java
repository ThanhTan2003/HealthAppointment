package com.programmingtechie.doctor_service.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.programmingtechie.doctor_service.mapper.DoctorMapper;
import com.programmingtechie.doctor_service.repository.httpClient.MedicalClient;
import org.springframework.data.domain.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.programmingtechie.doctor_service.dto.response.DoctorResponse;
import com.programmingtechie.doctor_service.dto.response.PageResponse;
import com.programmingtechie.doctor_service.dto.response.QualificationResponse;
import com.programmingtechie.doctor_service.dto.response.SpecialtyResponse;
import com.programmingtechie.doctor_service.model.Doctor;
import com.programmingtechie.doctor_service.model.DoctorSpecialty;
import com.programmingtechie.doctor_service.model.Qualification;
import com.programmingtechie.doctor_service.model.Specialty;
import com.programmingtechie.doctor_service.repository.DoctorRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DoctorServiceV1 {
    final DoctorRepository doctorRepository;
    final WebClient.Builder webClientBuilder;

    final MedicalClient medicalClient;

    final DoctorMapper doctorMapper;

    // Lấy tất cả danh sách bác sĩ với phân trang
    public PageResponse<DoctorResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Doctor> pageData = doctorRepository.findAll(pageable);

        List<DoctorResponse> doctorResponses =
                pageData.getContent().stream().map(doctorMapper::toDoctorResponse).collect(Collectors.toList());

        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(doctorResponses)
                .build();
    }

    // Lấy bác sĩ theo ID
    public DoctorResponse getById(String id) {
        if ((id == null) || (id.isEmpty())) throw new IllegalArgumentException("Vui lòng cung cấp mã bác sĩ!");
        Doctor doctor = doctorRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ có mã " + id + "!"));
        return doctorMapper.toDoctorResponse(doctor);
    }

    // Lấy bác sĩ theo số điện thoại
    public DoctorResponse getByPhoneNumber(String phoneNumber) {
        Doctor doctor = doctorRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new IllegalArgumentException("Không tìm thấy bác sĩ có số điện thoại: " + phoneNumber));
        return doctorMapper.toDoctorResponse(doctor);
    }

    // Lấy danh sách bác sĩ theo Specialty với phân trang
    public PageResponse<DoctorResponse> getDoctorsBySpecialty(String specialtyId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Doctor> pageData = doctorRepository.findDoctorsBySpecialty(specialtyId, pageable);

        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(doctorMapper::toDoctorResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // Lấy danh sách bác sĩ theo Qualification với phân trang
    public PageResponse<DoctorResponse> getDoctorsByQualification(
            String qualificationAbbreviation, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Doctor> pageData = doctorRepository.findDoctorsByQualification(qualificationAbbreviation, pageable);

        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(doctorMapper::toDoctorResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // Tìm kiếm bác sĩ theo từ khóa và phân trang
    public PageResponse<DoctorResponse> searchDoctors1(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Doctor> pageData = doctorRepository.searchDoctors(keyword, pageable);

        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(doctorMapper::toDoctorResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // Tìm kiếm bác sĩ theo từ khóa và phân trang sử dụng extension unaccent
    public PageResponse<DoctorResponse> searchDoctors(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Doctor> pageData = doctorRepository.searchDoctorsWithUnaccent(keyword, pageable);

        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(doctorMapper::toDoctorResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public PageResponse<DoctorResponse> getAllDoctorsWithServiceTimeFrame(int page, int size) {
        // Gọi Medical Service để lấy danh sách các doctorId có ServiceTimeFrame
        PageResponse<String> response = medicalClient.getDoctorsWithServiceTimeFrames(page, size);

        List<String> doctorIdsWithService = response.getData();

        // Tìm danh sách bác sĩ dựa trên danh sách doctorId đã có
        List<Doctor> doctors = doctorRepository.findByIdIn(doctorIdsWithService);

        List<DoctorResponse> doctorResponses = doctors.stream()
                .map(doctorMapper::toDoctorResponsePublic)
                .collect(Collectors.toList());

        return PageResponse.<DoctorResponse>builder()
                .currentPage(response.getCurrentPage())
                .pageSize(response.getPageSize())
                .totalPages(response.getTotalPages())
                .totalElements(response.getTotalElements())
                .data(doctorResponses)
                .build();
    }

    public List<DoctorResponse> getByIds(List<String> doctorIds) {
        List<Doctor> doctors = doctorRepository.findByIdIn(doctorIds);
        return doctors.stream()
                .map(doctorMapper::toDoctorResponse)
                .collect(Collectors.toList());
    }

}
