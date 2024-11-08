package com.programmingtechie.identity_service.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.programmingtechie.identity_service.dto.response.Doctor.DoctorResponse;

@FeignClient(name = "doctor-client", url = "http://localhost:8080/api/v1/doctor")
public interface DoctorClient {

    @PostMapping(value = "/id/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    DoctorResponse getDoctorById(@PathVariable("doctorId") String doctorId);
}
