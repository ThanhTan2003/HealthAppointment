package com.programmingtechie.appointment_service.mapper;

import java.time.LocalDateTime;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.request.AppointmentRequest;
import com.programmingtechie.appointment_service.dto.response.AppointmentResponse;
import com.programmingtechie.appointment_service.model.Appointment;
import com.programmingtechie.appointment_service.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentMapper {
    final AppointmentRepository appointmentRepository;

    public Appointment toAppointmentEntity(AppointmentRequest appointmentRequest) {
        // Chuyển đổi từ AppointmentRequest sang Appointmecnt entity
        return Appointment.builder()
                .id(createAppointmentId())
                .dateTime(LocalDateTime.now())
                .date(appointmentRequest.getDate())
                .session(appointmentRequest.getSession())
                .status("Chờ phê duyệt")
                .serviceTimeFrameId(appointmentRequest.getServiceTimeFrameId())
                .patientsId(appointmentRequest.getPatientsId())
                .build();
    }

    private String createAppointmentId() {
        String prefix = "LH-";
        Random random = new Random();
        StringBuilder middlePart = new StringBuilder();
        StringBuilder suffixPart = new StringBuilder();

        //        // Tạo 10 chữ số ngẫu nhiên
        //        for (int i = 0; i < 10; i++) {
        //            middlePart.append(random.nextInt(10));
        //        }

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
                .session(appointment.getSession())
                .status(appointment.getStatus())
                .orderNumber(appointment.getOrderNumber())
                .lastUpdated(appointment.getLastUpdated())
                .serviceTimeFrameId(appointment.getServiceTimeFrameId())
                .medicalRecordsId(appointment.getMedicalRecordsId())
                .patientsId(appointment.getPatientsId())
                .replacementDoctorId(appointment.getReplacementDoctorId())
                .billId(appointment.getBill() != null ? appointment.getBill().getId() : null)
                .build();
    }
}
