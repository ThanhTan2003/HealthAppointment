package com.programmingtechie.appointment_service.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.persistence.criteria.Predicate;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.programmingtechie.appointment_service.dto.request.AppointmentCountRequest;
import com.programmingtechie.appointment_service.dto.request.AppointmentRequest;
import com.programmingtechie.appointment_service.dto.request.AppointmentSearchRequest;
import com.programmingtechie.appointment_service.dto.request.PaymentRequest;
import com.programmingtechie.appointment_service.dto.response.AppointmentCountResponse;
import com.programmingtechie.appointment_service.dto.response.AppointmentResponse;
import com.programmingtechie.appointment_service.dto.response.Medical.AppointmentTimeFrameResponse;
import com.programmingtechie.appointment_service.dto.response.Medical.ServiceTimeFrameInAppointmentResponse;
import com.programmingtechie.appointment_service.dto.response.PageResponse;
import com.programmingtechie.appointment_service.dto.response.Patient.PatientResponse;
import com.programmingtechie.appointment_service.dto.response.Payment.PaymentResponse;
import com.programmingtechie.appointment_service.mapper.AppointmentMapper;
import com.programmingtechie.appointment_service.model.Appointment;
import com.programmingtechie.appointment_service.repository.AppointmentRepository;
import com.programmingtechie.appointment_service.repository.httpClient.MedicalClient;
import com.programmingtechie.appointment_service.repository.httpClient.PatientClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;

    private final AppointmentMapper appointmentMapper;

    @Autowired
    @Lazy
    private final PaymentService paymentService;

    final PatientClient patientClient;
    final MedicalClient medicalClient;

    @Transactional
    public AppointmentResponse createAppointment(AppointmentRequest appointmentRequest) {
        String patientId = appointmentRequest.getPatientsId();
        String customerId = "";
        try {
            // Boolean patientExists = patientClient.checkPatientExists(patientId);
            PatientResponse patientResponse = patientClient.getByPatientId(patientId);

            log.info("patientResponse: " + patientResponse);
            if (patientResponse == null) throw new IllegalArgumentException("Hồ sơ không hợp lệ!");
            customerId = patientResponse.getCustomerId();

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
                if (!id.equals(customerId)) throw new IllegalArgumentException("Người dùng chưa được xác thực");
            }
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

        if (appointmentRepository.existsByPatientsIdAndServiceTimeFrameIdAndDate(patientId, serviceTimeFrameId, date)) {
            throw new IllegalArgumentException(
                    "Hồ sơ bệnh nhân này đã đặt lịch hẹn vào thời điểm này. Vui lòng chọn hồ sơ khác.");
        }

        // Kiểm tra ngày hẹn hợp lệ
        if (date.isBefore(today.plusDays(1))) {
            throw new IllegalArgumentException("Ngày đăng ký lịch hẹn phải sau ít nhất 1 ngày so với ngày hiện tại.");
        }

        if (date.isAfter(today.plusMonths(1))) {
            throw new IllegalArgumentException("Ngày đăng ký lịch hẹn không được quá 1 tháng kể từ ngày hiện tại.");
        }

        try {
            List<Integer> existingOrderNumbers =
                    appointmentRepository.findOrderNumbersByServiceTimeFrameIdAndDate(serviceTimeFrameId, date);
            Integer nextOrderNumber =
                    medicalClient.getNextAvailableOrderNumber(serviceTimeFrameId, date, existingOrderNumbers);
            log.info("Next available order number: " + nextOrderNumber);

            if (nextOrderNumber == -1) {
                throw new IllegalArgumentException("Dịch vụ bác sĩ không tồn tại!");
            } else if (nextOrderNumber == 0) {
                throw new IllegalArgumentException("Dịch vụ đã đủ đăng ký!");
            }

            // Tạo lịch hẹn mới
            Appointment appointment = appointmentMapper.toAppointmentEntity(appointmentRequest);
            appointment.setCustomerId(customerId);
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
                // Trường hợp thông báo lỗi có chuỗi xác định như "Dịch vụ bác sĩ không tồn
                // tại!"
                if (e.getMessage().contains("Dịch vụ bác sĩ không tồn tại!")
                        || e.getMessage().contains("Dịch vụ đã đủ đăng ký!")) {
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

    public List<String> getBookedPatientIds(List<String> patientsId, String serviceTimeFrameId, LocalDate date) {
        return appointmentRepository
                .findAllByPatientIdInAndServiceTimeFrameIdAndDate(patientsId, serviceTimeFrameId, date)
                .stream()
                .map(Appointment::getPatientsId)
                .collect(Collectors.toList());
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

    public PageResponse<AppointmentResponse> getAllAppointments(int page, int size, String status, String id) {
        Pageable pageable = PageRequest.of(page - 1, size);

        // Nếu status là chuỗi rỗng, chuyển thành "empty" để tìm kiếm
        if (status == null || status.isEmpty()) {
            status = ""; // Truyền chuỗi rỗng cho trường hợp không có status
        }

        // Lấy danh sách các cuộc hẹn từ repository
        Page<Appointment> appointments = appointmentRepository.getAllAppointment(pageable, status, id);

        // Chuyển đổi thành AppointmentResponse
        List<AppointmentResponse> data = appointments.getContent().stream()
                .map(appointmentMapper::toAppointmentResponse)
                .collect(Collectors.toList());

        // Lấy danh sách ServiceTimeFrameIds từ các AppointmentResponse
        List<String> serviceTimeFrameIds =
                data.stream().map(AppointmentResponse::getServiceTimeFrameId).collect(Collectors.toList());

        try {
            // Lấy thông tin ServiceTimeFrame từ MedicalClient
            List<ServiceTimeFrameInAppointmentResponse> serviceTimeFrames = medicalClient.getByIds(serviceTimeFrameIds);

            // Tạo một map ánh xạ từ serviceTimeFrameId sang ServiceTimeFrame
            Map<String, ServiceTimeFrameInAppointmentResponse> serviceTimeFrameMap = serviceTimeFrames.stream()
                    .collect(Collectors.toMap(
                            ServiceTimeFrameInAppointmentResponse::getId, serviceTimeFrame -> serviceTimeFrame));

            // Cập nhật thông tin ServiceTimeFrame vào mỗi AppointmentResponse
            data.forEach(item -> {
                ServiceTimeFrameInAppointmentResponse serviceTimeFrame =
                        serviceTimeFrameMap.get(item.getServiceTimeFrameId());
                if (serviceTimeFrame != null) {
                    item.setServiceTimeFrame(serviceTimeFrame);
                }
            });
        } catch (Exception e) {
            log.info("Lỗi kết nối đến Medical Service: " + e.getMessage());
        }

        // Trả về kết quả với pagination
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

    // Tính tổng số lượng appointments từ một danh sách các cặp serviceTimeFrameId
    // và date
    public List<AppointmentCountResponse> countAppointments(List<AppointmentCountRequest> request) {
        List<AppointmentCountResponse> responses = new ArrayList<>();

        for (AppointmentCountRequest item : request) {
            long count = appointmentRepository.countByServiceTimeFrameIdAndDate(
                    item.getServiceTimeFrameId(), item.getDate());

            AppointmentCountResponse response =
                    new AppointmentCountResponse(item.getServiceTimeFrameId(), item.getDate(), count);

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
                predicates.add(
                        criteriaBuilder.equal(root.get("replacementDoctorId"), request.getReplacementDoctorId()));
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

    public List<String> getDistinctStatuses() {
        return appointmentRepository.findDistinctStatuses();
    }

    public PaymentResponse register(AppointmentRequest appointmentRequest, HttpServletRequest httpServletRequest) {
        String patientId = appointmentRequest.getPatientsId();
        String customerId = "";
        try {
            PatientResponse patientResponse = patientClient.getByPatientId(patientId);

            // log.info("patientExists: " + patientExists);

            log.info("patientResponse: " + patientResponse);
            if (patientResponse == null) throw new IllegalArgumentException("Hồ sơ không hợp lệ!");
            customerId = patientResponse.getCustomerId();

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
                if (!id.equals(customerId)) throw new IllegalArgumentException("Người dùng chưa được xác thực");
            }
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

        if (appointmentRepository.existsByPatientsIdAndServiceTimeFrameIdAndDate(patientId, serviceTimeFrameId, date)) {
            throw new IllegalArgumentException(
                    "Hồ sơ bệnh nhân này đã đặt lịch hẹn vào thời điểm này. Vui lòng chọn hồ sơ khác.");
        }

        // Kiểm tra ngày hẹn hợp lệ
        if (date.isBefore(today.plusDays(1))) {
            throw new IllegalArgumentException("Ngày đăng ký lịch hẹn phải sau ít nhất 1 ngày so với ngày hiện tại.");
        }

        if (date.isAfter(today.plusMonths(1))) {
            throw new IllegalArgumentException("Ngày đăng ký lịch hẹn không được quá 1 tháng kể từ ngày hiện tại.");
        }

        try {
            List<Integer> existingOrderNumbers =
                    appointmentRepository.findOrderNumbersByServiceTimeFrameIdAndDate(serviceTimeFrameId, date);
            Integer nextOrderNumber =
                    medicalClient.getNextAvailableOrderNumber(serviceTimeFrameId, date, existingOrderNumbers);
            log.info("Next available order number: " + nextOrderNumber);

            if (nextOrderNumber == -1) {
                throw new IllegalArgumentException("Dịch vụ bác sĩ không tồn tại!");
            } else if (nextOrderNumber == 0) {
                throw new IllegalArgumentException("Dịch vụ đã đủ đăng ký!");
            }

            Double unitPrice = medicalClient.getUnitPriceById(appointmentRequest.getServiceTimeFrameId());

            // Đăng ký và yêu cầu thanh toán
            PaymentRequest paymentRequest = PaymentRequest.builder()
                    .customerId(customerId)
                    .unitPrice(unitPrice)
                    .orderNumber(nextOrderNumber)
                    .appointmentRequest(appointmentRequest)
                    .build();

            log.info("(paymentRequest: " + paymentRequest.toString());
            return paymentService.payment(paymentRequest, httpServletRequest);

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
                // Trường hợp thông báo lỗi có chuỗi xác định như "Dịch vụ bác sĩ không tồn
                // tại!"
                if (e.getMessage().contains("Dịch vụ bác sĩ không tồn tại!")
                        || e.getMessage().contains("Dịch vụ đã đủ đăng ký!")) {
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
}
