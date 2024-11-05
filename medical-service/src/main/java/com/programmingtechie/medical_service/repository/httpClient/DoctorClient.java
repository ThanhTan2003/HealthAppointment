package com.programmingtechie.medical_service.repository.httpClient;

import com.programmingtechie.medical_service.config.AuthenticationRequestInterceptor;
import com.programmingtechie.medical_service.dto.response.SpecialtyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(
        name = "specialty-client",
        url = "${app.services.specialty}",
        configuration = { AuthenticationRequestInterceptor.class })

public interface DoctorClient {

    @GetMapping(value = "/specialty/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SpecialtyResponse getSpecialtyById(
            @PathVariable String id);
}
