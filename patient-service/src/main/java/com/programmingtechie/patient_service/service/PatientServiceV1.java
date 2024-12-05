package com.programmingtechie.patient_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.patient_service.dto.request.PatientCreationRequest;
import com.programmingtechie.patient_service.dto.request.PatientUpdateRequest;
import com.programmingtechie.patient_service.dto.response.CustomerIdentityResponse;
import com.programmingtechie.patient_service.dto.response.CustomerWithPatientDetailsResponse;
import com.programmingtechie.patient_service.dto.response.PageResponse;
import com.programmingtechie.patient_service.dto.response.PatientAndCustomerInfoResponse;
import com.programmingtechie.patient_service.dto.response.PatientResponse;
import com.programmingtechie.patient_service.mapper.PatientMapper;
import com.programmingtechie.patient_service.model.Patient;
import com.programmingtechie.patient_service.repository.PatientRepository;
import com.programmingtechie.patient_service.repository.httpClient.AppointmentClient;
import com.programmingtechie.patient_service.repository.httpClient.CustomerIdentityClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PatientServiceV1 {
    final PatientRepository patientRepository;
    final PatientMapper patientMapper;
    final CustomerIdentityClient customerIdentityClient;
    private final AppointmentClient appointmentClient;

    public PatientResponse createPatient(PatientCreationRequest patientRequest) {
        Patient patient = patientMapper.mapToPatientRequest(patientRequest);
        patientRepository.save(patient);
        return patientMapper.mapToPatientResponse(patient);
    }

    public PatientResponse updatePatient(String patientId, PatientUpdateRequest patientUpdateRequest) {
        Patient patient = patientRepository
                .findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy thông tin!"));

        patient = Patient.builder()
                .fullName(patientUpdateRequest.getFullName())
                .dateOfBirth(patientUpdateRequest.getDateOfBirth())
                .gender(patientUpdateRequest.getGender())
                .insuranceId(patientUpdateRequest.getInsuranceId())
                .identificationCode(patientUpdateRequest.getIdentificationCode())
                .nation(patientUpdateRequest.getNation())
                .occupation(patientUpdateRequest.getOccupation())
                .phoneNumber(patientUpdateRequest.getPhoneNumber())
                .email(patientUpdateRequest.getEmail())
                .country(patientUpdateRequest.getCountry())
                .province(patientUpdateRequest.getProvince())
                .district(patientUpdateRequest.getDistrict())
                .ward(patientUpdateRequest.getWard())
                .address(patientUpdateRequest.getAddress())
                .relationship(patientUpdateRequest.getRelationship())
                .note(patientUpdateRequest.getNote())
                .build();

        patientRepository.save(patient);

        return patientMapper.mapToPatientResponse(patient);
    }

    public void deletePatient(String patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new IllegalArgumentException("Không tìm thấy thông tin!");
        }
        patientRepository.deleteById(patientId);
    }

    public PatientResponse getByPatientId(String patientId) {
        if ((patientId == null) || (patientId.isEmpty())) throw new IllegalArgumentException("Không tìm thấy hồ sơ!");
        Patient patient = patientRepository
                .findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy hồ sơ với mã " + patientId + "!"));
        return patientMapper.mapToPatientResponse(patient);
    }

    public boolean isValidpatient(PatientCreationRequest patientRequest) {
        return isValidInsuranceId(patientRequest.getInsuranceId())
                && isValidIdentificationCode(patientRequest.getIdentificationCode())
                && isValidPhoneNumber(patientRequest.getPhoneNumber());
    }

    private boolean isValidInsuranceId(String insuranceId) {
        return insuranceId != null && insuranceId.length() == 15;
    }

    private boolean isValidIdentificationCode(String identificationCode) {
        return identificationCode != null && identificationCode.length() == 12;
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && phoneNumber.length() == 10;
    }

    public PageResponse<PatientResponse> getPatientByCustomerId(String customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Patient> pageData = patientRepository.findPatientByCustomerId(customerId, pageable);
        return PageResponse.<PatientResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(patientMapper::mapToPatientResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public PageResponse<PatientResponse> getMyPatientRecord(int page, int size) {
        var context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Người dùng chưa được xác thực");
        }

        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
            // Lấy thông tin từ Jwt
            String id = jwt.getClaim("id");
            if (id == null) {
                throw new IllegalArgumentException("Không tìm thấy ID trong token!");
            }

            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Patient> pageData = patientRepository.findPatientByCustomerId(id, pageable);
            return PageResponse.<PatientResponse>builder()
                    .currentPage(page)
                    .pageSize(pageData.getSize())
                    .totalPages(pageData.getTotalPages())
                    .totalElements(pageData.getTotalElements())
                    .data(pageData.getContent().stream()
                            .map(patientMapper::mapToPatientResponse)
                            .collect(Collectors.toList()))
                    .build();
        }

        throw new IllegalArgumentException("Principal không hợp lệ hoặc không phải là JWT");
    }

    public PageResponse<PatientResponse> getUnbookedPatientRecords(
            String serviceTimeFrameId, LocalDate date, int page, int size) {
        var context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new IllegalArgumentException("Người dùng chưa được xác thực");
        }
        if (authentication.getPrincipal() instanceof org.springframework.security.oauth2.jwt.Jwt jwt) {
            String customerId = jwt.getClaim("id");
            if (customerId == null) {
                throw new IllegalArgumentException("Không tìm thấy ID trong token!");
            }
            Pageable pageable = PageRequest.of(page - 1, size);
            Page<Patient> pageData = patientRepository.findPatientByCustomerId(customerId, pageable);
            List<String> allPatientIds =
                    pageData.getContent().stream().map(Patient::getId).collect(Collectors.toList());
            // Gọi Appointment Service để lấy danh sách patientIds đã đặt lịch
            List<String> bookedPatientIds =
                    appointmentClient.getBookedPatientIds(allPatientIds, serviceTimeFrameId, date);
            // Lọc những hồ sơ chưa đặt lịch
            List<Patient> unbookedPatients = pageData.getContent().stream()
                    .filter(patient -> !bookedPatientIds.contains(patient.getId()))
                    .collect(Collectors.toList());
            return PageResponse.<PatientResponse>builder()
                    .currentPage(page)
                    .pageSize(unbookedPatients.size())
                    .totalPages(pageData.getTotalPages())
                    .totalElements(unbookedPatients.size())
                    .data(unbookedPatients.stream()
                            .map(patientMapper::mapToPatientResponse)
                            .collect(Collectors.toList()))
                    .build();
        }
        throw new IllegalArgumentException("Principal không hợp lệ hoặc không phải là JWT");
    }

    public PageResponse<PatientResponse> getPatientByCustomerEmail(String email, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Patient> pageData = patientRepository.findPatientByEmail(email, pageable);
        return PageResponse.<PatientResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(patientMapper::mapToPatientResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public PageResponse<PatientResponse> getPatientByCustomerPhoneNumber(String phoneNumber, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Patient> pageData = patientRepository.findPatientByPhoneNumber(phoneNumber, pageable);
        return PageResponse.<PatientResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(pageData.getContent().stream()
                        .map(patientMapper::mapToPatientResponse)
                        .collect(Collectors.toList()))
                .build();
    }

    public PageResponse<PatientAndCustomerInfoResponse> getPatientWithCustomerInfo(
            String customerId, int page, int size) {

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Patient> pageData = patientRepository.findPatientByCustomerId(customerId, pageable);

        List<PatientAndCustomerInfoResponse> patientResponses = pageData.getContent().stream()
                .map(patientMapper::toPatientResponse)
                .collect(Collectors.toList());

        return PageResponse.<PatientAndCustomerInfoResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(patientResponses)
                .build();
    }

    public CustomerWithPatientDetailsResponse getCustomerWithPatientDetails(String customerId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        // Lấy danh sách bệnh nhân phân trang từ repository
        Page<Patient> pageData = patientRepository.findByCustomerId(customerId, pageable);

        List<PatientResponse> patientResponses = pageData.getContent().stream()
                .map(patientMapper::mapToPatientResponse)
                .collect(Collectors.toList());

        PageResponse<PatientResponse> pageResponse = PageResponse.<PatientResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(patientResponses)
                .build();

        // Lấy thông tin khách hàng từ service
        CustomerIdentityResponse customerIdentityResponse;
        try {
            customerIdentityResponse = customerIdentityClient.getByCustomerId(customerId);
        } catch (Exception e) {
            log.error("Error fetching customer info: " + e.getMessage());
            throw new RuntimeException("Unable to fetch customer details");
        }

        // Tạo response cuối cùng với dữ liệu phân trang
        return CustomerWithPatientDetailsResponse.builder()
                .id(customerIdentityResponse.getId())
                .fullName(customerIdentityResponse.getFullName())
                .dateOfBirth(customerIdentityResponse.getDateOfBirth())
                .gender(customerIdentityResponse.getGender())
                .phoneNumber(customerIdentityResponse.getPhoneNumber())
                .email(customerIdentityResponse.getEmail())
                .status(customerIdentityResponse.getStatus())
                .lastAccessTime(customerIdentityResponse.getLastAccessTime())
                .lastUpdated(customerIdentityResponse.getLastUpdated())
                .patientDetails(pageResponse)
                .build();
    }

    public boolean doesPatientExist(String id) {
        return patientRepository.existsById(id);
    }
}
