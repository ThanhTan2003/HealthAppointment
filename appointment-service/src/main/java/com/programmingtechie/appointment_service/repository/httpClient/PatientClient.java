package com.programmingtechie.appointment_service.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.programmingtechie.appointment_service.config.AuthenticationRequestInterceptor;
import com.programmingtechie.appointment_service.dto.response.Patient.PatientResponse;

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
