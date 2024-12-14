package com.programmingtechie.HIS.repository.httpClient;

import com.programmingtechie.HIS.dto.response.AppointmentSyncResponse;
import com.programmingtechie.HIS.dto.response.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@FeignClient(
        name = "appointment-client",
        url = "${app.services.appointment}")
public interface AppointmentClient {
    @GetMapping("/public/sync/from-his")
    public PageResponse<AppointmentSyncResponse> getAppointmentsForHIS(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam("expiryDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime expiryDateTime,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("hmac") String hmac);

}
