package com.programmingtechie.appointment_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.dto.request.SendEmail.AppointmentBookingInfo;
import com.programmingtechie.appointment_service.dto.request.SendEmail.AppointmentConfirmation;
import com.programmingtechie.appointment_service.dto.request.SendEmail.DoctorReplacementNotification;
import com.programmingtechie.appointment_service.dto.request.SendEmail.EmailMessage;
import com.programmingtechie.appointment_service.service.EmailSenderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/appointment/send-email")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class EmailController {
    private final EmailSenderService emailSenderService;

    @PostMapping
    public ResponseEntity sendEmail(@RequestBody EmailMessage emailMessage) {
        this.emailSenderService.sendEmail(emailMessage.getTo(), emailMessage.getSubject(), emailMessage.getMessage());
        return ResponseEntity.ok("Success");
    }

    @PostMapping("/appointment-booking-info")
    public ResponseEntity sendEmailAppointmentBookingInfo(@RequestBody AppointmentBookingInfo appointmentBookingInfo) {
        emailSenderService.sendEmailAppointmentBookingInfo(appointmentBookingInfo);
        return ResponseEntity.ok("Email Sent Successfully");
    }

    @GetMapping("/appointment-booking-info/test")
    public ResponseEntity sendEmailAppointmentBookingInfo() {
        emailSenderService.sendEmailAppointmentBookingInfo();
        return ResponseEntity.ok("Email Sent Successfully");
    }

    @PostMapping("/appointment-confirmation")
    public ResponseEntity sendEmailAppointmentConfirmation(
            @RequestBody AppointmentConfirmation appointmentConfirmation) {
        emailSenderService.sendEmailAppointmentConfirmation(appointmentConfirmation);
        return ResponseEntity.ok("Email Sent Successfully");
    }

    @GetMapping("/appointment-confirmation/test")
    public ResponseEntity sendEmailAppointmentConfirmation() {
        emailSenderService.sendEmailAppointmentConfirmation();
        return ResponseEntity.ok("Email Sent Successfully");
    }

    @PostMapping("/doctor-replacement-notification")
    public ResponseEntity sendDoctorReplacementNotification(
            @RequestBody DoctorReplacementNotification doctorReplacementNotification) {
        emailSenderService.sendDoctorReplacementNotification(doctorReplacementNotification);
        return ResponseEntity.ok("Doctor Replacement Notification Email Sent Successfully");
    }

    @GetMapping("/doctor-replacement-notification/test")
    public ResponseEntity sendSampleDoctorReplacementNotification() {
        emailSenderService.sendDoctorReplacementNotification();
        return ResponseEntity.ok("Sample Doctor Replacement Notification Email Sent Successfully");
    }
}
