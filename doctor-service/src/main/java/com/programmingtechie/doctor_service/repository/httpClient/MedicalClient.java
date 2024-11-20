package com.programmingtechie.doctor_service.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.programmingtechie.doctor_service.config.AuthenticationRequestInterceptor;
import com.programmingtechie.doctor_service.dto.response.PageResponse;

@FeignClient(
        name = "medical-client",
        url = "${app.services.medical}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface MedicalClient {

    // Lấy danh sách các doctorId có ServiceTimeFrame, được phân trang
    @GetMapping(value = "/service-time-frame/public/get-doctor-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    PageResponse<String> getDoctorsWithServiceTimeFrames(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size);

    @GetMapping(value = "/service-time-frame/public/get-specialty-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    PageResponse<String> getListSpecialtyWithServiceTimeFrames(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size);
}
