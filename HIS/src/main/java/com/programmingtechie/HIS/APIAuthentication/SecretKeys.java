package com.programmingtechie.HIS.APIAuthentication;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class SecretKeys {
    @Value("${appointment.secretKey}")
    private String appointmentSecretKey;

    @Value("${doctor.secretKey}")
    private String doctorSecretKey;

    @Value("${his.secretKey}")
    private String hisSecretKey;

    @Value("${medical.secretKey}")
    private String medicalSecretKey;

    @Value("${identity.secretKey}")
    private String identitySecretKey;

    @Value("${patient.secretKey}")
    private String patientSecretKey;

    @Value("${notification.secretKey}")
    private String notificationSecretKey;
}
