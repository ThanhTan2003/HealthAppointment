package com.programmingtechie.appointment_service.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.appointment_service.dto.request.AppointmentRequest;
import com.programmingtechie.appointment_service.dto.response.AppointmentResponse;
import com.programmingtechie.appointment_service.dto.response.AppointmentTimeFrameResponse;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.mapper.AppointmentMapper;
import com.programmingtechie.appointment_service.model.Appointment;
import com.programmingtechie.appointment_service.repository.AppointmentRepositorynt_service;
import com.programmingtechie.appointment_service.repository.httpClient.MedicalClient;
import com.programmingtechie.appointment_service.repository.httpClient.PatientClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {
    final AppointmentRepositorynt_service appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    final PatientClient patientClient;
    final MedicalClient medicalClient;

    public AppointmentResponse createAppointment(AppointmentRequest appointmentRequest) {

        String patientId = appointmentRequest.getPatientsId();
        try {
            Boolean patientExists = patientClient.checkPatientExists(patientId);
            log.info("patientExists: " + patientExists.toString());
            log.info("!patientExists: " + (!patientExists));
            if (!patientExists) throw new IllegalArgumentException("Hồ sơ không hợp lệ!");
        } catch (IllegalArgumentException e) {
            // Nếu là lỗi logic từ dữ liệu không hợp lệ, tái ném ngoại lệ gốc
            throw e;
        } catch (Exception e) {
            // Nếu là lỗi kết nối, ghi log và ném ngoại lệ khác
            log.info("Lỗi kết nối đến Patient Service: " + e.getMessage());
            throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại sau!");
        }

        String serviceTimeFrameId = appointmentRequest.getServiceTimeFrameId();
        try {
            Boolean serviceTimeFrameExists = medicalClient.checkServiceTimeFrameExists(serviceTimeFrameId);
            if (!serviceTimeFrameExists) throw new IllegalArgumentException("Dịch vụ không hợp lệ!");

        } catch (IllegalArgumentException e) {
            // Nếu là lỗi logic từ dữ liệu không hợp lệ, tái ném ngoại lệ gốc
            throw e;
        } catch (Exception e) {
            log.info("Lỗi kết nối đến Medical Service: " + e.getMessage());
            throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại sau!");
        }

        Appointment appointment = appointmentMapper.toAppointmentEntity(appointmentRequest);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    public AppointmentResponse getAppointmentById(String id) {
        Appointment appointment = appointmentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy cuộc hẹn với ID: " + id));
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    public AppointmentResponse updateAppointment(String id, AppointmentRequest appointmentRequest) {
        Appointment appointment = appointmentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy cuộc hẹn với ID: " + id));
        // appointmentMapper.updateAppointmentEntity(appointment, appointmentRequest);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    @Transactional
    public AppointmentResponse updateAppointmentStatus(String id, String status) {
        Appointment appointment = appointmentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy cuộc hẹn với ID: " + id));
        appointment.setStatus(status);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    @Transactional
    public AppointmentResponse updateServiceTimeFrameId(String id, String serviceTimeFrameId) {
        Appointment appointment = appointmentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy cuộc hẹn với ID: " + id));
        appointment.setServiceTimeFrameId(serviceTimeFrameId);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    @Transactional
    public AppointmentResponse updateReplacementDoctorId(String id, String replacementDoctorId) {
        Appointment appointment = appointmentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy cuộc hẹn với ID: " + id));
        appointment.setReplacementDoctorId(replacementDoctorId);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    @Transactional
    public AppointmentResponse updateAppointment(String id, Map<String, Object> updates) {
        Appointment appointment = appointmentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy cuộc hẹn với ID: " + id));

        // Duyệt qua các cặp trường và giá trị trong `updates` để cập nhật thực thể
        // `appointment`
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();

            switch (key) {
                case "status":
                    appointment.setStatus((String) value);
                    break;
                case "orderNumber":
                    appointment.setOrderNumber((Integer) value);
                    break;
                case "serviceTimeFrameId":
                    appointment.setServiceTimeFrameId((String) value);
                    break;
                case "replacementDoctorId":
                    appointment.setReplacementDoctorId((String) value);
                    break;
                default:
                    throw new IllegalArgumentException("Trường cập nhật không hợp lệ: " + key);
            }
        }

        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    @Transactional
    public AppointmentResponse updateAppointmentOrderNumber(String id, Integer orderNumber) {
        Appointment appointment = appointmentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy cuộc hẹn với ID: " + id));
        appointment.setOrderNumber(orderNumber);
        appointment = appointmentRepository.save(appointment);
        return appointmentMapper.toAppointmentResponse(appointment);
    }

    public void deleteAppointment(String id) {
        Appointment appointment = appointmentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy cuộc hẹn với ID: " + id));
        appointmentRepository.delete(appointment);
    }

    public PageResponse<AppointmentResponse> getAllAppointments(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Appointment> appointments = appointmentRepository.findAll(pageable);
        List<AppointmentResponse> data = appointments.stream()
                .map(appointmentMapper::toAppointmentResponse)
                .toList();
        return PageResponse.<AppointmentResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(appointments.getTotalPages())
                .totalElements(appointments.getTotalElements())
                .data(data)
                .build();
    }

    public PageResponse<AppointmentTimeFrameResponse> getMyAppointment(int page, int size) {
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
            Page<Appointment> pageData = appointmentRepository.findByCustomerId(id, pageable);
            return PageResponse.<AppointmentTimeFrameResponse>builder()
                    .currentPage(page)
                    .pageSize(pageData.getSize())
                    .totalPages(pageData.getTotalPages())
                    .totalElements(pageData.getTotalElements())
                    .data(pageData.getContent().stream()
                            .map(appointmentMapper::mapToAppointmentTimeFrameResponse)
                            .collect(Collectors.toList()))
                    .build();
        }
        throw new IllegalArgumentException("Principal không hợp lệ hoặc không phải là JWT");
    }

    public PageResponse<AppointmentTimeFrameResponse> getAppointmentByCustomerIdAndPatientsId(
            String patientId, int page, int size) {
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
            Page<Appointment> pageData = appointmentRepository.findByCustomerIdAndPatientsId(id, patientId, pageable);
            return PageResponse.<AppointmentTimeFrameResponse>builder()
                    .currentPage(page)
                    .pageSize(pageData.getSize())
                    .totalPages(pageData.getTotalPages())
                    .totalElements(pageData.getTotalElements())
                    .data(pageData.getContent().stream()
                            .map(appointmentMapper::mapToAppointmentTimeFrameResponse)
                            .collect(Collectors.toList()))
                    .build();
        }
        throw new IllegalArgumentException("Principal không hợp lệ hoặc không phải là JWT");
    }

    public PageResponse<AppointmentResponse> getAppointmentByPatientsId(String id, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Appointment> appointments = appointmentRepository.findByPatientsId(id, pageable);
        List<AppointmentResponse> data = appointments.stream()
                .map(appointmentMapper::toAppointmentResponse)
                .toList();
        return PageResponse.<AppointmentResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(appointments.getTotalPages())
                .totalElements(appointments.getTotalElements())
                .data(data)
                .build();
    }

    public PageResponse<AppointmentResponse> searchAppointments(
            String id, LocalDate date, String serviceTimeFrameId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Specification<Appointment> specification = Specification.where(null);

        if (id != null && !id.isBlank()) { // Nếu id không phải null hoặc rỗng, thêm điều kiện id = :id
            specification =
                    specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("id"), id));
        }
        if (date != null) {
            specification =
                    specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("date"), date));
        }
        if (serviceTimeFrameId != null && !serviceTimeFrameId.isBlank()) {
            specification = specification.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("serviceTimeFrameId"), serviceTimeFrameId));
        }

        Page<Appointment> appointments = appointmentRepository.findAll(specification, pageable);

        List<AppointmentResponse> data = appointments.stream()
                .map(appointmentMapper::toAppointmentResponse)
                .toList();

        return PageResponse.<AppointmentResponse>builder()
                .currentPage(page)
                .pageSize(size)
                .totalPages(appointments.getTotalPages())
                .totalElements(appointments.getTotalElements())
                .data(data)
                .build();
    }
}
