package com.programmingtechie.medical_service.repository.httpClient;

import com.programmingtechie.medical_service.config.AuthenticationRequestInterceptor;
import com.programmingtechie.medical_service.dto.request.AppointmentCountRequest;
import com.programmingtechie.medical_service.dto.response.AppointmentCountResponse;
import com.programmingtechie.medical_service.dto.response.Doctor.DoctorResponse;
import com.programmingtechie.medical_service.dto.response.Doctor.SpecialtyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@FeignClient(
        name = "appointment-client",
        url = "${app.services.appointment}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface AppointmentClient {


    @PostMapping(value = "/public/count-appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<AppointmentCountResponse> countAppointments(@RequestBody List<AppointmentCountRequest> request);

    @GetMapping(value = "/public/count-appointments", produces = MediaType.APPLICATION_JSON_VALUE)
    public AppointmentCountResponse countAppointmentsByParams(
            @RequestParam String serviceTimeFrameId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date);
}

