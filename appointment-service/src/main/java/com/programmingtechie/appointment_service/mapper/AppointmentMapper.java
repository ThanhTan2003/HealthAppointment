package com.programmingtechie.appointment_service.mapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import org.springframework.stereotype.Component;

import com.programmingtechie.appointment_service.dto.request.AppointmentCreateRequest;
import com.programmingtechie.appointment_service.dto.request.AppointmentRequest;
import com.programmingtechie.appointment_service.dto.response.AppointmentResponse;
import com.programmingtechie.appointment_service.dto.response.AppointmentSyncResponse;
import com.programmingtechie.appointment_service.dto.response.His.HealthCheckResultResponse;
import com.programmingtechie.appointment_service.dto.response.Medical.AppointmentTimeFrameResponse;
import com.programmingtechie.appointment_service.dto.response.Medical.ServiceTimeFrameResponse;
import com.programmingtechie.appointment_service.model.Appointment;
import com.programmingtechie.appointment_service.repository.AppointmentRepository;
import com.programmingtechie.appointment_service.repository.httpClient.MedicalClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentMapper {
    final AppointmentRepository appointmentRepository;

    final HealthCheckResultMapper healthCheckResultMapper;

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

    public Appointment toAppointmentEntity(AppointmentCreateRequest appointmentRequest) {

        // Chuyển đổi từ AppointmentRequest sang Appointmecnt entity
        return Appointment.builder()
                .id(createAppointmentId())
                .dateTime(LocalDateTime.now())
                .date(appointmentRequest.getDate())
                .status("Chờ phê duyệt")
                .serviceTimeFrameId(appointmentRequest.getServiceTimeFrameId())
                .patientsId(appointmentRequest.getPatientsId())
                .customerId(appointmentRequest.getCustomerId())
                .orderNumber(appointmentRequest.getOrderNumber())
                .payment(appointmentRequest.getPayment())
                .build();
    }

    public String createAppointmentId() {
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

        // Lấy tên ngày (Tên thứ) và định dạng ngày
        String dateName = getFormattedDateName(appointment.getDate());

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .dateTime(appointment.getDateTime())
                .dateTimeFullName(getFormattedDateTime(appointment.getDateTime()))
                .date(appointment.getDate())
                .dateName(appointment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .dateFullName(getFormattedDateName(appointment.getDate()))
                .status(appointment.getStatus())
                .orderNumber(appointment.getOrderNumber())
                .lastUpdated(appointment.getLastUpdated())
                .serviceTimeFrameId(appointment.getServiceTimeFrameId())
                .patientsId(appointment.getPatientsId())
                .replacementDoctorId(appointment.getReplacementDoctorId())
                .customerId(appointment.getCustomerId())
                .checkResultResponseList(null)
                .paymentId(
                        appointment.getPayment() != null
                                ? appointment.getPayment().getId()
                                : null)
                .build();
    }

    public AppointmentResponse toAppointmentResponseAndHealthCheckResultResponse(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment không được null");
        }

        // Lấy tên ngày (Tên thứ) và định dạng ngày
        String dateName = getFormattedDateName(appointment.getDate());

        List<HealthCheckResultResponse> healthCheckResultResponses = appointment.getHealthCheckResults().stream()
                .map(healthCheckResultMapper::toHealthCheckResultResponse)
                .toList();

        return AppointmentResponse.builder()
                .id(appointment.getId())
                .dateTime(appointment.getDateTime())
                .dateTimeFullName(getFormattedDateTime(appointment.getDateTime()))
                .date(appointment.getDate())
                .dateName(appointment.getDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))
                .dateFullName(getFormattedDateName(appointment.getDate()))
                .status(appointment.getHealthCheckResults().size() > 0 ? "Đã có kết quả" : appointment.getStatus())
                .orderNumber(appointment.getOrderNumber())
                .lastUpdated(appointment.getLastUpdated())
                .serviceTimeFrameId(appointment.getServiceTimeFrameId())
                .patientsId(appointment.getPatientsId())
                .replacementDoctorId(appointment.getReplacementDoctorId())
                .customerId(appointment.getCustomerId())
                .checkResultResponseList(healthCheckResultResponses)
                .paymentId(
                        appointment.getPayment() != null
                                ? appointment.getPayment().getId()
                                : null)
                .build();
    }

    public String getFormattedDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }
        // Định dạng ngày giờ là dd/MM/yyyy hh:mm:ss
        return dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    // Hàm nhận vào LocalDate và trả về chuỗi định dạng "Tên ngày - ngày/ tháng/năm"
    private String getFormattedDateName(LocalDate date) {
        if (date == null) {
            return "";
        }

        // Định dạng ngày là ngày/tháng/năm
        String formattedDate = date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // Lấy tên ngày trong tuần (e.g. Thứ hai)
        String dayOfWeek =
                date.getDayOfWeek().getDisplayName(java.time.format.TextStyle.FULL, Locale.forLanguageTag("vi"));

        return dayOfWeek + " - " + formattedDate;
    }

    public AppointmentTimeFrameResponse mapToAppointmentTimeFrameResponse(Appointment appointment) {
        if (appointment == null) {
            throw new IllegalArgumentException("Appointment không được null");
        }
        ServiceTimeFrameResponse serviceTimeFrameResponse = null;
        try {
            serviceTimeFrameResponse = medicalClient.getAppointmentTimeFrame(appointment.getServiceTimeFrameId());
        } catch (Exception e) {
            log.info("Lỗi giao tiếp đến Medical service: " + e.getMessage());
        }
        return AppointmentTimeFrameResponse.builder()
                .id(appointment.getId())
                .dateTime(appointment.getDateTime())
                .date(appointment.getDate())
                .status(appointment.getHealthCheckResults().size() > 0 ? "Đã có kết quả" : appointment.getStatus())
                .orderNumber(appointment.getOrderNumber())
                .lastUpdated(appointment.getLastUpdated())
                .patientsId(appointment.getPatientsId())
                .customerId(appointment.getCustomerId())
                .replacementDoctorId(appointment.getReplacementDoctorId())
                .paymentId(
                        appointment.getPayment() != null
                                ? appointment.getPayment().getId()
                                : null)
                .serviceTimeFrameResponse(serviceTimeFrameResponse)
                .build();
    }

    public AppointmentSyncResponse toAppointmentSyncResponse(Appointment appointment) {
        return AppointmentSyncResponse.builder()
                .id(appointment.getId())
                .dateTime(appointment.getDateTime())
                .date(appointment.getDate())
                .status(appointment.getStatus())
                .orderNumber(appointment.getOrderNumber())
                .lastUpdated(appointment.getLastUpdated())
                .patientsId(appointment.getPatientsId())
                .customerId(appointment.getCustomerId())
                .replacementDoctorId(appointment.getReplacementDoctorId())
                .build();
    }
}
