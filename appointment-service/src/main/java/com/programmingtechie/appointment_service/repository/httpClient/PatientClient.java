package com.programmingtechie.appointment_service.repository.httpClient;

import com.programmingtechie.appointment_service.dto.response.Patient.PatientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.programmingtechie.appointment_service.config.AuthenticationRequestInterceptor;

@FeignClient(
        name = "patient-client",
        url = "${app.services.patient}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface PatientClient {

    @GetMapping(value = "/exists/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean checkPatientExists(@PathVariable String id);

    @GetMapping("/id/{patientId}")
    public PatientResponse getByPatientId(@PathVariable String patientId);
}
