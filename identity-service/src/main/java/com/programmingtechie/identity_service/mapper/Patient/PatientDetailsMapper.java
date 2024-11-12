package com.programmingtechie.identity_service.mapper.Patient;

import org.springframework.stereotype.Component;

import com.programmingtechie.identity_service.repository.httpClient.Patient.PatientClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class PatientDetailsMapper {
    private final PatientClient patientClient;
}