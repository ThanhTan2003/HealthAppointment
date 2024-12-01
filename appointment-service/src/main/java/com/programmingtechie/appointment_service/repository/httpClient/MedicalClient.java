package com.programmingtechie.appointment_service.repository.httpClient;

import com.programmingtechie.appointment_service.dto.response.Medical.ServiceTimeFrameResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.programmingtechie.appointment_service.config.AuthenticationRequestInterceptor;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@FeignClient(
        name = "medical-client",
        url = "${app.services.medical}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface MedicalClient {

    @GetMapping(value = "/service-time-frame/exists/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean checkServiceTimeFrameExists(@PathVariable String id);

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceTimeFrameResponse getServiceTimeFrameById(@PathVariable String id);

    @GetMapping(value = "/service-time-frame/public/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ServiceTimeFrameResponse getAppointmentTimeFrame(@PathVariable String id);

    @GetMapping(value = "/service-time-frame/next-order/{serviceTimeFrameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer getNextAvailableOrderNumber(
            @PathVariable String serviceTimeFrameId,
            @RequestParam LocalDate day,
            @RequestParam List<Integer> existingOrderNumbers);
}
