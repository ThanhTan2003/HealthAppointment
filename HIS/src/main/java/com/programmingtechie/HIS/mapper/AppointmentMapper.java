package com.programmingtechie.HIS.mapper;

import com.programmingtechie.HIS.dto.response.AppointmentResponse;
import com.programmingtechie.HIS.dto.response.HealthCheckResultResponse;
import com.programmingtechie.HIS.model.Appointment;
import com.programmingtechie.HIS.model.HealthCheckResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentMapper {
    final DoctorMapper doctorMapper;
    final HealthCheckResultMapper healthCheckResultMapper;

    public AppointmentResponse toAppointmentResponse(Appointment appointment)
    {
        AppointmentResponse appointmentResponse =  AppointmentResponse.builder()
                .id(appointment.getId())
                .dateTime(appointment.getDateTime())
                .date(appointment.getDate())
                .dateName(getFormattedDateName(appointment.getDate()))
                .orderNumber(appointment.getOrderNumber())
                .lastUpdated(appointment.getLastUpdated())
                .room(appointment.getRoom())
                .service(appointment.getService())
                .doctor(doctorMapper.toDoctorResponse(appointment.getDoctor()))
                .doctorServiceId(appointment.getDoctorServiceId())
                .medicalRecordsId(appointment.getMedicalRecordsId())
                .patientsId(appointment.getPatientsId())
                .customerId(appointment.getCustomerId())
                .replacementDoctorId(appointment.getReplacementDoctorId())
                .build();
        List<HealthCheckResultResponse> healthCheckResultList = new ArrayList<>();
        for(HealthCheckResult item: appointment.getHealthCheckResults())
        {
            HealthCheckResultResponse healthCheckResult = healthCheckResultMapper.toHealthCheckResultResponse(item);
            healthCheckResultList.add(healthCheckResult);
        }
        appointmentResponse.setHealthCheckResults(healthCheckResultList);

        // Kiểm tra xem có kết quả kiểm tra sức khỏe hay không để set status
        if (healthCheckResultList.isEmpty()) {
            appointmentResponse.setStatus("Chưa nhập kết quả");
        } else {
            appointmentResponse.setStatus("Đã nhập kết quả");
        }
        return appointmentResponse;
    }

    private String getFormattedDateName(java.time.LocalDate date) {
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
}
