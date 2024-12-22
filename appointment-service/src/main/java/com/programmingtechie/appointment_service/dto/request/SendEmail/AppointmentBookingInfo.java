package com.programmingtechie.appointment_service.dto.request.SendEmail;

import com.programmingtechie.appointment_service.dto.response.Medical.ServiceTimeFrameResponse;
import com.programmingtechie.appointment_service.dto.response.Patient.PatientResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class AppointmentBookingInfo {

    private String id;
    private String patientId;
    private String patientName;
    private String gender;
    private String insuranceNumber;
    private String phoneNumber;
    private String serviceName;
    private String dateTime;
    private String date;
    private String timeFrame;
    private String doctorName;
    private String paymentAmount;
    private String paymentStatus;
    private String status;
    private String emailAddress;

    private PatientResponse patientResponse;
    private ServiceTimeFrameResponse serviceTimeFrameResponse;
}
