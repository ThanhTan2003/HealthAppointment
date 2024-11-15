package com.programmingtechie.doctor_service.service;

import java.util.List;
import java.util.stream.Collectors;

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
import com.programmingtechie.doctor_service.repository.httpClient.MedicalClient;

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

    // Lấy tất cả danh sách bác sĩ với phân trang
    public PageResponse<DoctorResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Doctor> pageData = doctorRepository.findAll(pageable);

        List<DoctorResponse> doctorResponses =
                pageData.getContent().stream().map(this::mapToDoctorResponse).collect(Collectors.toList());

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
        return mapToDoctorResponse(doctor);
    }

    // Lấy bác sĩ theo số điện thoại
    public DoctorResponse getByPhoneNumber(String phoneNumber) {
        Doctor doctor = doctorRepository
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new IllegalArgumentException("Không tìm thấy bác sĩ có số điện thoại: " + phoneNumber));
        return mapToDoctorResponse(doctor);
    }

    private String getQualificationName(Doctor doctor) {
        // Lấy danh sách các học vị (abbreviation) từ doctor.getDoctorQualifications()
        return doctor.getDoctorQualifications().stream()
                .map(doctorQualification ->
                        doctorQualification.getQualification().getAbbreviation())
                //                .sorted()
                // Optional: nếu cần sắp xếp theo thứ tự, bạn có thể tùy chỉnh.
                .collect(Collectors.joining(". ")) // Thay dấu chấm thành ". " để không có dấu chấm cuối cùng
                .trim(); // Loại bỏ khoảng trắng thừa nếu có
    }

    // Hàm chuyển đổi từ Doctor sang DoctorResponse
    private DoctorResponse mapToDoctorResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .qualificationName(getQualificationName(doctor))
                .gender(doctor.getGender())
                .phoneNumber(doctor.getPhoneNumber())
                .email(doctor.getEmail())
                .description(doctor.getDescription())
                .status(doctor.getStatus())
                .image(doctor.getImage())
                .lastUpdated(doctor.getLastUpdated())
                .specialties(doctor.getSpecialties().stream()
                        .map(this::mapToSpecialtyResponse)
                        .collect(Collectors.toList()))
                .qualifications(doctor.getDoctorQualifications().stream()
                        .map(doctorQualification -> mapToQualificationResponse(doctorQualification.getQualification()))
                        .collect(Collectors.toList()))
                .build();
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
                        .map(this::mapToDoctorResponse)
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
                        .map(this::mapToDoctorResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    // Hàm chuyển đổi từ Specialty sang SpecialtyResponse
    private SpecialtyResponse mapToSpecialtyResponse(DoctorSpecialty doctorSpecialty) {
        Specialty specialty = doctorSpecialty.getSpecialty();
        return SpecialtyResponse.builder()
                .specialtyId(specialty.getId())
                .specialtyName(specialty.getName())
                .description(specialty.getDescription())
                .build();
    }

    // Hàm chuyển đổi từ Qualification sang QualificationResponse
    private QualificationResponse mapToQualificationResponse(Qualification qualification) {
        return QualificationResponse.builder()
                .abbreviation(qualification.getAbbreviation())
                .name(qualification.getName())
                .displayOrder(qualification.getDisplayOrder())
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
                        .map(this::mapToDoctorResponse)
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
                        .map(this::mapToDoctorResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public PageResponse<DoctorResponse> getAllDoctorsWithServiceTimeFrame(int page, int size) {
        // Gọi Medical Service để lấy danh sách các doctorId có ServiceTimeFrame
        PageResponse<String> response = medicalClient.getDoctorsWithServiceTimeFrames(page, size);

        List<String> doctorIdsWithService = response.getData();

        // Tìm danh sách bác sĩ dựa trên danh sách doctorId đã có
        List<Doctor> doctors = doctorRepository.findByIdIn(doctorIdsWithService);

        List<DoctorResponse> doctorResponses =
                doctors.stream().map(this::mapToDoctorResponse).collect(Collectors.toList());

        return PageResponse.<DoctorResponse>builder()
                .currentPage(response.getCurrentPage())
                .pageSize(response.getPageSize())
                .totalPages(response.getTotalPages())
                .totalElements(response.getTotalElements())
                .data(doctorResponses)
                .build();
    }
}
