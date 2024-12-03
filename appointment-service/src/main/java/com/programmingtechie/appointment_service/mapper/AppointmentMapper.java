package com.programmingtechie.appointment_service.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Random;

import com.programmingtechie.appointment_service.dto.response.Medical.AppointmentTimeFrameResponse;
import com.programmingtechie.appointment_service.dto.response.Medical.ServiceTimeFrameResponse;
import com.programmingtechie.appointment_service.repository.httpClient.MedicalClient;
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
    final MedicalClient medicalClient;

    public Appointment toAppointmentEntity(AppointmentRequest appointmentRequest) {

        // Chuyển đổi từ AppointmentRequest sang Appointmecnt entity
        return Appointment.builder()
                .id(createAppointmentId())
                .dateTime(LocalDateTime.now())
                .date(appointmentRequest.getDate())
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

        // Lấy tên ngày (Tên thứ) và định dạng ngày
        String dateName = getFormattedDateName(appointment.getDate());

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .dateTime(appointment.getDateTime())
                .date(appointment.getDate())
                .dateName(appointment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .status(appointment.getStatus())
                .orderNumber(appointment.getOrderNumber())
                .lastUpdated(appointment.getLastUpdated())
                .serviceTimeFrameId(appointment.getServiceTimeFrameId())
                .medicalRecordsId(appointment.getMedicalRecordsId())
                .patientsId(appointment.getPatientsId())
                .replacementDoctorId(appointment.getReplacementDoctorId())
                .customerId(appointment.getCustomerId())
                .billId(appointment.getBill() != null ? appointment.getBill().getId() : null)
                .build();
    }

    // Hàm nhận vào LocalDate và trả về chuỗi định dạng "Tên ngày - ngày/ tháng/năm"
    private String getFormattedDateName(LocalDate date) {
        if (date == null) {
            return "";
        }

        // Định dạng ngày là ngày/tháng/năm
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Lấy tên ngày trong tuần (e.g. Thứ hai)
        String dayOfWeek = date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, Locale.forLanguageTag("vi"));

        return dayOfWeek + " - " + formattedDate;
    }

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
