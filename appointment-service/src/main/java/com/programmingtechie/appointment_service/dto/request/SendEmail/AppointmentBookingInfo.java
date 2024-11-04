package com.programmingtechie.appointment_service.dto.request.SendEmail;

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
}
