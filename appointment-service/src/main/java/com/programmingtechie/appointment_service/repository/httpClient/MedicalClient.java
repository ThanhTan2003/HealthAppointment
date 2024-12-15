package com.programmingtechie.appointment_service.repository.httpClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.programmingtechie.appointment_service.dto.response.Medical.ServiceTimeFrameInSyncResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.appointment_service.config.AuthenticationRequestInterceptor;
import com.programmingtechie.appointment_service.dto.response.Medical.ServiceTimeFrameInAppointmentResponse;
import com.programmingtechie.appointment_service.dto.response.Medical.ServiceTimeFrameResponse;

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

    @GetMapping(
            value = "/service-time-frame/next-order/{serviceTimeFrameId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer getNextAvailableOrderNumber(
            @PathVariable String serviceTimeFrameId,
            @RequestParam LocalDate day,
            @RequestParam List<Integer> existingOrderNumbers);

    @GetMapping("/service-time-frame/get-unit-price/{id}")
    public Double getUnitPriceById(@PathVariable String id);

    @PostMapping(value = "/service-time-frame/get-by-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ServiceTimeFrameInAppointmentResponse> getByIds(
            @RequestParam("expiryDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiryDateTime,
            @RequestParam("hmac") String hmac,
            @RequestBody List<String> ids);

    @PostMapping("/service-time-frame/public/get-by-ids")
    public List<ServiceTimeFrameInSyncResponse> getByIdsPublic(@RequestBody List<String> ids);
}
