package com.programmingtechie.HIS.repository.httpClient;

import java.time.LocalDateTime;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.programmingtechie.HIS.dto.response.AppointmentSyncResponse;
import com.programmingtechie.HIS.dto.response.PageResponse;

@FeignClient(name = "appointment-client", url = "${app.services.appointment}")
public interface AppointmentClient {
    @GetMapping("/public/sync/from-his")
    public PageResponse<AppointmentSyncResponse> getAppointmentsForHIS(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam("expiryDateTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    LocalDateTime expiryDateTime,
            @RequestParam("page") int page,
            @RequestParam("size") int size,
            @RequestParam("hmac") String hmac);
}
