package com.programmingtechie.identity_service.repository.httpClient;

import com.programmingtechie.identity_service.dto.response.Doctor.SpecialtyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.programmingtechie.identity_service.dto.response.Doctor.DoctorResponse;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "doctor-client", url = "http://localhost:8080/api/v1/doctor")
public interface DoctorClient {

    @PostMapping(value = "/id/{doctorId}", produces = MediaType.APPLICATION_JSON_VALUE)
    DoctorResponse getDoctorById(@PathVariable("doctorId") String doctorId);
}