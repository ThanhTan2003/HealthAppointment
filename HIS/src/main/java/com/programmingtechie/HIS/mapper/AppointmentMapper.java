package com.programmingtechie.HIS.mapper;

import com.programmingtechie.HIS.dto.response.AppointmentResponse;
import com.programmingtechie.HIS.model.Appointment;
import com.programmingtechie.HIS.model.HealthCheckResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppointmentMapper {
    final DoctorMapper doctorMapper;

    public AppointmentResponse toAppointmentResponse(Appointment appointment)
    {
        AppointmentResponse appointmentResponse =  AppointmentResponse.builder()
                .id(appointment.getId())
                .dateTime(appointment.getDateTime())
                .date(appointment.getDate())
                .status(appointment.getStatus())
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
        List<HealthCheckResult> healthCheckResultList = new ArrayList<>();
        for(HealthCheckResult item: appointment.getHealthCheckResults())
        {
            HealthCheckResult healthCheckResult = HealthCheckResult.builder()
                    .id(item.getId())
                    .appointment(null)
                    .name(item.getName())
                    .url(item.getUrl())
                    .build();
            healthCheckResultList.add(healthCheckResult);
        }
        appointmentResponse.setHealthCheckResults(healthCheckResultList);
        return appointmentResponse;
    }
}
