package com.programmingtechie.appointment_service.dto.request.SendEmail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class DoctorReplacementNotification {
    private String id;

    private String patientName;

    private String gender;

    private String insuranceNumber;

    private String phoneNumber;

    private String serviceName;

    private String date;

    private String timeFrame;

    private String doctorName;

    private String roomName;

    private String orderNumber;

    private String status;

    private String newDoctorName;

    private String NewRoomName;

    private String newTimeFrame;

    private String newOrderNumber;

    private String emailAddress;
}
