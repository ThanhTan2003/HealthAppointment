package com.programmingtechie.patient_service.repository.httpClient;

import java.time.LocalDate;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.programmingtechie.patient_service.config.AuthenticationRequestInterceptor;

@FeignClient(
        name = "appointment-client",
        url = "${app.services.appointment}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface AppointmentClient {

    @GetMapping(value = "/patient-exists", produces = MediaType.APPLICATION_JSON_VALUE)
    List<String> getBookedPatientIds(
            @RequestParam List<String> patientsId,
            @RequestParam String serviceTimeFrameId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);
}
