package com.programmingtechie.appointment_service.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.appointment_service.dto.request.AppointmentCountRequest;
import com.programmingtechie.appointment_service.dto.request.AppointmentSearchRequest;
import com.programmingtechie.appointment_service.dto.response.AppointmentCountResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.appointment_service.dto.request.AppointmentRequest;
import com.programmingtechie.appointment_service.dto.response.AppointmentResponse;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.mapper.AppointmentMapper;
import com.programmingtechie.appointment_service.model.Appointment;
import com.programmingtechie.appointment_service.repository.AppointmentRepository;
import com.programmingtechie.appointment_service.repository.httpClient.MedicalClient;
import com.programmingtechie.appointment_service.repository.httpClient.PatientClient;

import jakarta.persistence.criteria.Predicate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {
    final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    final PatientClient patientClient;
    final MedicalClient medicalClient;

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest appointmentRequest) {
        String patientId = appointmentRequest.getPatientsId();

        try {
            Boolean patientExists = patientClient.checkPatientExists(patientId);
            log.info("patientExists: " + patientExists);
            if (!patientExists) throw new IllegalArgumentException("Hồ sơ không hợp lệ!");
        } catch (IllegalArgumentException e) {
            log.error("Lỗi khi kiểm tra bệnh nhân: " + e.getMessage());
            throw new IllegalArgumentException(e.getMessage());
        } catch (Exception e) {
            log.error("Lỗi kết nối đến Patient Service: " + e.getMessage());
            throw new IllegalArgumentException("Đã xảy ra lỗi. Vui lòng thử lại sau!");
        }

        String serviceTimeFrameId = appointmentRequest.getServiceTimeFrameId();
        LocalDate date = appointmentRequest.getDate();
        LocalDate today = LocalDate.now();

        // Kiểm tra ngày hẹn hợp lệ
        if (date.isBefore(today.plusDays(1))) {
            throw new IllegalArgumentException("Ngày đăng ký lịch hẹn phải sau ít nhất 1 ngày so với ngày hiện tại.");
        }

        if (date.isAfter(today.plusMonths(1))) {
            throw new IllegalArgumentException("Ngày đăng ký lịch hẹn không được quá 1 tháng kể từ ngày hiện tại.");
        }

        try {
            List<Integer> existingOrderNumbers = appointmentRepository.findOrderNumbersByServiceTimeFrameIdAndDate(serviceTimeFrameId, date);
            Integer nextOrderNumber = medicalClient.getNextAvailableOrderNumber(serviceTimeFrameId, date, existingOrderNumbers);
            log.info("Next available order number: " + nextOrderNumber);

            if (nextOrderNumber == -1) {
                throw new IllegalArgumentException("Dịch vụ bác sĩ không tồn tại!");
            } else if (nextOrderNumber == 0) {
                throw new IllegalArgumentException("Dịch vụ đã đủ đăng ký!");
            }

            // Tạo lịch hẹn mới
            Appointment appointment = appointmentMapper.toAppointmentEntity(appointmentRequest);
            appointment.setOrderNumber(nextOrderNumber);
            appointment = appointmentRepository.save(appointment);

            return appointmentMapper.toAppointmentResponse(appointment);

        } catch (HttpClientErrorException e) {
            // Lỗi client (4xx)
            log.error("Lỗi từ client: " + e.getMessage());
            log.error("Full error response: " + e.getResponseBodyAsString());

            try {
                // Trích xuất thông báo lỗi từ JSON
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode errorJson = objectMapper.readTree(e.getResponseBodyAsString());
                String errorMessage = errorJson.path("message").asText();

                log.error("Thông báo lỗi từ client: " + errorMessage);
                throw new IllegalArgumentException(errorMessage); // Ném thông báo lỗi chi tiết
            } catch (JsonProcessingException ex) {
                log.error("Không thể trích xuất thông báo lỗi từ JSON: " + ex.getMessage());
                throw new IllegalArgumentException("Đã xảy ra lỗi khi tạo lịch hẹn.");
            }
        } catch (HttpServerErrorException e) {
            // Lỗi server (5xx)
            log.error("Lỗi từ server: " + e.getMessage());
            log.error("Full error response: " + e.getResponseBodyAsString());

            try {
                // Trích xuất thông báo lỗi từ JSON nếu có
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode errorJson = objectMapper.readTree(e.getResponseBodyAsString());
                String errorMessage = errorJson.path("message").asText();

                log.error("Thông báo lỗi từ server: " + errorMessage);
                throw new IllegalArgumentException("Lỗi từ dịch vụ. Vui lòng thử lại sau.");
            } catch (JsonProcessingException ex) {
                log.error("Không thể trích xuất thông báo lỗi từ JSON: " + ex.getMessage());
                throw new IllegalArgumentException("Lỗi từ dịch vụ. Vui lòng thử lại sau.");
            }
        } catch (Exception e) {
            // In log lỗi chung
            log.error("Lỗi khi tạo lịch hẹn: " + e.getMessage());

            // Kiểm tra nếu message có dạng thông báo lỗi cụ thể
            if (e.getMessage() != null) {
                // Trường hợp thông báo lỗi có chuỗi xác định như "Dịch vụ bác sĩ không tồn tại!"
                if (e.getMessage().contains("Dịch vụ bác sĩ không tồn tại!") || e.getMessage().contains("Dịch vụ đã đủ đăng ký!")) {
                    throw new IllegalArgumentException(e.getMessage());
                }

                // Trường hợp thông báo lỗi có định dạng JSON
                try {
                    // Phân tích lỗi JSON để lấy thông báo cụ thể từ trường "message"
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode errorJson = objectMapper.readTree(e.getMessage());
                    String errorMessage = errorJson.path("message").asText();

                    // Ném lại thông báo lỗi đã trích xuất từ JSON
                    if (!errorMessage.isEmpty()) {
                        throw new IllegalArgumentException(errorMessage);
                    } else {
                        // Nếu không tìm thấy message trong JSON, ném lỗi mặc định
                        throw new IllegalArgumentException("Đã xảy ra lỗi khi tạo lịch hẹn. Vui lòng kiểm tra lại");
                    }
                } catch (JsonProcessingException ex) {
                    log.error("Không thể trích xuất thông báo lỗi từ JSON: " + ex.getMessage());
                    throw new IllegalArgumentException("Lỗi từ dịch vụ. Vui lòng thử lại sau.");
                }
            } else {
                // Nếu không có message hoặc không xác định được thông báo
                log.error("Thông báo lỗi không rõ ràng hoặc không có thông báo lỗi.");
                throw new IllegalArgumentException("Đã xảy ra lỗi khi tạo lịch hẹn. Vui lòng kiểm tra lại");
            }
        }
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

        // Duyệt qua các cặp trường và giá trị trong `updates` để cập nhật thực thể `appointment`
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

    public PageResponse<AppointmentResponse> getAppointmentByPatientsId(String id, int page, int size) {
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

    // Tính tổng số lượng appointments từ một danh sách các cặp serviceTimeFrameId và date
    public List<AppointmentCountResponse> countAppointments(List<AppointmentCountRequest> request) {
        List<AppointmentCountResponse> responses = new ArrayList<>();

        for (AppointmentCountRequest item : request) {
            long count = appointmentRepository.countByServiceTimeFrameIdAndDate(
                    item.getServiceTimeFrameId(), item.getDate());

            AppointmentCountResponse response = new AppointmentCountResponse(
                    item.getServiceTimeFrameId(), item.getDate(), count);

            responses.add(response);
        }

        return responses;
    }

    // Tính tổng số lượng appointments theo một cặp serviceTimeFrameId và date
    public AppointmentCountResponse countAppointmentsByParams(String serviceTimeFrameId, LocalDate date) {
        long totalAppointments = appointmentRepository.countByServiceTimeFrameIdAndDate(serviceTimeFrameId, date);

        return new AppointmentCountResponse(serviceTimeFrameId, date, totalAppointments);
    }

    public boolean checkIfAppointmentExists(AppointmentSearchRequest request) {
        // Tạo điều kiện truy vấn động
        Specification<Appointment> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Kiểm tra từng trường một cách linh hoạt
            if (request.getMedicalRecordsId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("medicalRecordsId"), request.getMedicalRecordsId()));
            }
            if (request.getServiceTimeFrameId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("serviceTimeFrameId"), request.getServiceTimeFrameId()));
            }
            if (request.getPatientsId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("patientsId"), request.getPatientsId()));
            }
            if (request.getReplacementDoctorId() != null) {
                predicates.add(criteriaBuilder.equal(root.get("replacementDoctorId"), request.getReplacementDoctorId()));
            }

            // Nếu không có điều kiện nào, trả về true (kết quả hợp lệ)
            if (predicates.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Sử dụng Spring Data JPA Specification để truy vấn cơ sở dữ liệu
        return appointmentRepository.exists(specification);
    }
}