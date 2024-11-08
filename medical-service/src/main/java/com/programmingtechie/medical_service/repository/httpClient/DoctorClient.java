package com.programmingtechie.medical_service.repository.httpClient;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.programmingtechie.medical_service.config.AuthenticationRequestInterceptor;
import com.programmingtechie.medical_service.dto.response.Doctor.DoctorResponse;
import com.programmingtechie.medical_service.dto.response.Doctor.SpecialtyResponse;

@FeignClient(
        name = "doctor-client",
        url = "${app.services.doctor}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface DoctorClient {

    @GetMapping(value = "/specialty/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public SpecialtyResponse getSpecialtyById(@PathVariable String id);

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DoctorResponse getById(@PathVariable String id);

    @PostMapping(value = "/specialty/get-by-ids", produces = MediaType.APPLICATION_JSON_VALUE)
    List<SpecialtyResponse> getSpecialtiesByIds(@RequestBody List<String> specialtyIds);
}
