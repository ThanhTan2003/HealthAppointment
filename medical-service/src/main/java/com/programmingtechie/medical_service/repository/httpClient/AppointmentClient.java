package com.programmingtechie.medical_service.repository.httpClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.medical_service.config.AuthenticationRequestInterceptor;
import com.programmingtechie.medical_service.dto.request.AppointmentCountRequest;
import com.programmingtechie.medical_service.dto.response.AppointmentCountResponse;

@FeignClient(
        name = "appointment-client",
        url = "http://localhost:8080/api/v1/appointment",
        configuration = {AuthenticationRequestInterceptor.class})
public interface AppointmentClient {

    @PostMapping(value = "/public/count-appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppointmentCountResponse> countAppointments(@RequestBody List<AppointmentCountRequest> request);

    @GetMapping(value = "/public/count-appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppointmentCountResponse countAppointmentsByParams(
            @RequestParam String serviceTimeFrameId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);


}
