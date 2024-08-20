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

    public void createDoctor(DoctorRequest doctorRequest) {
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
                .qualification(doctorRequest.getQualification())
                .position(doctorRequest.getPosition())
                .description(doctorRequest.getDescription())
                .status(doctorRequest.getStatus())
                .image(doctorRequest.getImage())
                .roomId(doctorRequest.getRoomId())
                .build();
        try {
            doctorRepository.save(doctor);
        }
        catch (Exception e)
        {
            throw new IllegalArgumentException("Không thể thêm thông tin bác sĩ mới. Vui lòng thử lại sau!");
        }
    }

    public List<DoctorResponse> getAll()
    {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.stream().map(doctor -> DoctorResponse.builder()
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
                .qualification(doctor.getQualification())
                .position(doctor.getPosition())
                .description(doctor.getDescription())
                .status(doctor.getStatus())
                .image(doctor.getImage())
                .lastUpdated(doctor.getLastUpdated())
                .roomId(doctor.getRoomId())
                .build()
        ).collect(Collectors.toList());
    }

    public PageResponse<DoctorResponse> getAll(int page, int size) {
        // Tạo đối tượng Sort theo thứ tự tăng dần của fullName
        Sort sort = Sort.by("fullName").ascending();

        // Tạo Pageable với page và size đầu vào
        Pageable pageable = PageRequest.of(page - 1, size, sort);

        // Lấy dữ liệu trang từ repository (lưu ý: kiểu Pageable phải từ Spring, không phải AWT)
        var pageData = doctorRepository.getAllDoctor(pageable);

        // Mapping dữ liệu từ entity Doctor sang DTO DoctorResponse
        List<DoctorResponse> doctorResponses = pageData.getContent().stream().map(doctor ->
                DoctorResponse.builder()
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
                        .qualification(doctor.getQualification())
                        .position(doctor.getPosition())
                        .description(doctor.getDescription())
                        .status(doctor.getStatus())
                        .image(doctor.getImage())
                        .lastUpdated(doctor.getLastUpdated())
                        .roomId(doctor.getRoomId())
                        .build()
        ).toList();

        // Trả về đối tượng PageResponse với các thông tin cần thiết
        return PageResponse.<DoctorResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(doctorResponses)
                .build();
    }
}
