package com.programmingtechie.doctor_service.service;

import com.programmingtechie.doctor_service.dto.request.DoctorRequest;
import com.programmingtechie.doctor_service.dto.response.DoctorResponse;
import com.programmingtechie.doctor_service.dto.response.PageResponse;
import com.programmingtechie.doctor_service.model.Doctor;
import com.programmingtechie.doctor_service.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DoctorServiceV1 {
    final DoctorRepository doctorRepository;
    final WebClient.Builder webClientBuilder;

    private void checkForDuplicates(DoctorRequest doctorRequest) {
        doctorRepository.findFirstByPhoneNumberOrEmailOrIdentificationCode(
                        doctorRequest.getPhoneNumber(), doctorRequest.getEmail(), doctorRequest.getIdentificationCode())
                .ifPresent(doctor -> {
                    if (doctor.getPhoneNumber().equals(doctorRequest.getPhoneNumber()))
                        throw new IllegalArgumentException("Số điện thoại đã được " + doctor.getFullName() + " sử dụng!");
                    if (doctor.getEmail().equals(doctorRequest.getEmail()))
                        throw new IllegalArgumentException("Email đã được " + doctor.getFullName() + " sử dụng!");
                    if (doctor.getIdentificationCode().equals(doctorRequest.getIdentificationCode()))
                        throw new IllegalArgumentException("Mã định danh đã được " + doctor.getFullName() + " sử dụng!");
                });
    }

    private DoctorResponse doctorMapToDoctorResponse(Doctor doctor) {
        return DoctorResponse.builder()
                .id(doctor.getId())
                .fullName(doctor.getFullName())
                .dateOfBirth(doctor.getDateOfBirth())
                .gender(doctor.getGender())
                .identificationCode(doctor.getIdentificationCode())
                .phoneNumber(doctor.getPhoneNumber())
                .email(doctor.getEmail())
                .provinceOrCity(doctor.getProvinceOrCity())
                .district(doctor.getDistrict())
                .wardOrCommune(doctor.getWardOrCommune())
                .address(doctor.getAddress())
                .education(doctor.getEducation())
                .qualificationId(doctor.getQualificationId())
                .position(doctor.getPosition())
                .description(doctor.getDescription())
                .status(doctor.getStatus())
                .image(doctor.getImage())
                .lastUpdated(doctor.getLastUpdated())
                .roomId(doctor.getRoomId())
                .build();
    }

    public void createDoctor(DoctorRequest doctorRequest) {
        checkForDuplicates(doctorRequest);
        Doctor doctor = Doctor.builder()
                .fullName(doctorRequest.getFullName())
                .dateOfBirth(doctorRequest.getDateOfBirth())
                .gender(doctorRequest.getGender())
                .identificationCode(doctorRequest.getIdentificationCode())
                .phoneNumber(doctorRequest.getPhoneNumber())
                .email(doctorRequest.getEmail())
                .provinceOrCity(doctorRequest.getProvinceOrCity())
                .district(doctorRequest.getDistrict())
                .wardOrCommune(doctorRequest.getWardOrCommune())
                .address(doctorRequest.getAddress())
                .education(doctorRequest.getEducation())
                .qualificationId(doctorRequest.getQualificationId())
                .position(doctorRequest.getPosition())
                .description(doctorRequest.getDescription())
                .status(doctorRequest.getStatus())
                .image(doctorRequest.getImage())
                .roomId(doctorRequest.getRoomId())
                .build();
        doctorRepository.save(doctor);
    }

    public List<DoctorResponse> getAll() {
        return doctorRepository.findAll().stream()
                .map(this::doctorMapToDoctorResponse)
                .collect(Collectors.toList());
    }

    public PageResponse<DoctorResponse> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("fullName").ascending());
        var pageData = doctorRepository.getAllDoctor(pageable);

        List<DoctorResponse> doctorResponses = pageData.getContent().stream()
                .map(this::doctorMapToDoctorResponse)
                .toList();

        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(doctorResponses)
                .build();
    }

    public void updateDoctor(String id, DoctorRequest doctorRequest) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ với ID: " + id));

        checkForDuplicates(doctorRequest);

        existingDoctor.setFullName(doctorRequest.getFullName());
        existingDoctor.setDateOfBirth(doctorRequest.getDateOfBirth());
        existingDoctor.setGender(doctorRequest.getGender());
        existingDoctor.setIdentificationCode(doctorRequest.getIdentificationCode());
        existingDoctor.setPhoneNumber(doctorRequest.getPhoneNumber());
        existingDoctor.setEmail(doctorRequest.getEmail());
        existingDoctor.setProvinceOrCity(doctorRequest.getProvinceOrCity());
        existingDoctor.setDistrict(doctorRequest.getDistrict());
        existingDoctor.setWardOrCommune(doctorRequest.getWardOrCommune());
        existingDoctor.setAddress(doctorRequest.getAddress());
        existingDoctor.setEducation(doctorRequest.getEducation());
        existingDoctor.setQualificationId(doctorRequest.getQualificationId());
        existingDoctor.setPosition(doctorRequest.getPosition());
        existingDoctor.setDescription(doctorRequest.getDescription());
        existingDoctor.setStatus(doctorRequest.getStatus());
        existingDoctor.setImage(doctorRequest.getImage());
        existingDoctor.setRoomId(doctorRequest.getRoomId());

        doctorRepository.save(existingDoctor);
    }

    public void deleteDoctor(String id) {
        Doctor existingDoctor = doctorRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy bác sĩ với ID: " + id));
        doctorRepository.delete(existingDoctor);
    }

    public DoctorResponse getById(String id) {
        return doctorRepository.findById(id)
                .map(this::doctorMapToDoctorResponse)
                .orElse(null);
    }

    public DoctorResponse getByPhoneNumber(String phoneNumber) {
        return doctorRepository.findByPhoneNumber(phoneNumber)
                .map(this::doctorMapToDoctorResponse)
                .orElse(null);
    }

    public DoctorResponse getByEmail(String email) {
        return doctorRepository.findByEmail(email)
                .map(this::doctorMapToDoctorResponse)
                .orElse(null);
    }

    public DoctorResponse getByGender(String gender) {
        return doctorRepository.findByGender(gender)
                .map(this::doctorMapToDoctorResponse)
                .orElse(null);
    }
}
