package com.programmingtechie.appointment_service.service;

import com.programmingtechie.appointment_service.dto.request.SendEmail.AppointmentBookingInfo;
import com.programmingtechie.appointment_service.dto.request.SendEmail.AppointmentConfirmation;
import com.programmingtechie.appointment_service.dto.request.SendEmail.DoctorReplacementNotification;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String message);

    void sendEmailAppointmentBookingInfo(AppointmentBookingInfo appointmentBookingInfo);

    void sendEmailAppointmentBookingInfo();

    void sendEmailAppointmentConfirmation(AppointmentConfirmation appointmentConfirmation);

    void sendEmailAppointmentConfirmation();

    void sendDoctorReplacementNotification(DoctorReplacementNotification notification);

    void sendDoctorReplacementNotification();

}
