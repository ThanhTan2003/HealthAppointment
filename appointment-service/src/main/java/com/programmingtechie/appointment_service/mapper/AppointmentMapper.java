package com.programmingtechie.appointment_service.mapper;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.request.AppointmentRequest;
import com.programmingtechie.appointment_service.dto.response.AppointmentResponse;
import com.programmingtechie.appointment_service.dto.response.AppointmentTimeFrameResponse;
import com.programmingtechie.appointment_service.dto.response.medical.ServiceTimeFrameResponse;
import com.programmingtechie.appointment_service.model.Appointment;
import com.programmingtechie.appointment_service.repository.AppointmentRepositorynt_service;
import com.programmingtechie.appointment_service.repository.httpClient.MedicalClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentMapper {
    final AppointmentRepositorynt_service appointmentRepository;
    final MedicalClient medicalClient;

    public Appointment toAppointmentEntity(AppointmentRequest appointmentRequest) {
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

            // Chuyển đổi từ AppointmentRequest sang Appointmecnt entity
            return Appointment.builder()
                    .id(createAppointmentId())
                    .dateTime(LocalDateTime.now())
                    .date(appointmentRequest.getDate())
                    .status("Chờ phê duyệt")
                    .serviceTimeFrameId(appointmentRequest.getServiceTimeFrameId())
                    .patientsId(appointmentRequest.getPatientsId())
                    .customerId(id)
                    .build();
        }
        throw new IllegalArgumentException("Principal không hợp lệ hoặc không phải là JWT");
    }

    private String createAppointmentId() {
        String prefix = "LH-";
        Random random = new Random();
        StringBuilder middlePart = new StringBuilder();
        StringBuilder suffixPart = new StringBuilder();

        // // Tạo 10 chữ số ngẫu nhiên
        // for (int i = 0; i < 10; i++) {
        // middlePart.append(random.nextInt(10));
        // }

        // Tạo 10 ký tự chữ hoa hoặc số ngẫu nhiên
        String characters = "06BDYZVR2XJAW5KLTQSI9MC8UHE1OFG34NP7";
        for (int i = 0; i < 20; i++) {
            suffixPart.append(characters.charAt(random.nextInt(characters.length())));
        }

        String appointmentId = prefix + middlePart.toString() + suffixPart.toString();

        // Kiểm tra ID có tồn tại hay không
        boolean exists = appointmentRepository.existsById(appointmentId);
        if (!exists) {
            return appointmentId;
        }

        // Nếu ID đã tồn tại, thử tạo lại
        return createAppointmentId();
    }

    public AppointmentResponse toAppointmentResponse(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment không được null");
        }

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .dateTime(appointment.getDateTime())
                .date(appointment.getDate())
                .status(appointment.getStatus())
                .orderNumber(appointment.getOrderNumber())
                .lastUpdated(appointment.getLastUpdated())
                .serviceTimeFrameId(appointment.getServiceTimeFrameId())
                .medicalRecordsId(appointment.getMedicalRecordsId())
                .patientsId(appointment.getPatientsId())
                .customerId(appointment.getCustomerId())
                .replacementDoctorId(appointment.getReplacementDoctorId())
                .billId(appointment.getBill() != null ? appointment.getBill().getId() : null)
                .build();
    }

    // Gọi qua medical service time frame để lấy thông tin khung giờ theo
    // serviceTimeFrameId
    public AppointmentTimeFrameResponse mapToAppointmentTimeFrameResponse(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment không được null");
        }
        ServiceTimeFrameResponse serviceTimeFrameResponse =
                medicalClient.getAppointmentTimeFrame(appointment.getServiceTimeFrameId());

        return AppointmentTimeFrameResponse.builder()
                .id(appointment.getId())
                .dateTime(appointment.getDateTime())
                .date(appointment.getDate())
                .status(appointment.getStatus())
                .orderNumber(appointment.getOrderNumber())
                .lastUpdated(appointment.getLastUpdated())
                .medicalRecordsId(appointment.getMedicalRecordsId())
                .patientsId(appointment.getPatientsId())
                .customerId(appointment.getCustomerId())
                .replacementDoctorId(appointment.getReplacementDoctorId())
                .billId(appointment.getBill() != null ? appointment.getBill().getId() : null)
                .serviceTimeFrameResponse(serviceTimeFrameResponse)
                .build();
    }
}
