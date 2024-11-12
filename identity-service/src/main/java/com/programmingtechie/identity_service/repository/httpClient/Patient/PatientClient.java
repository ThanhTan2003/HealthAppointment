package com.programmingtechie.identity_service.repository.httpClient.Patient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.programmingtechie.identity_service.config.AuthenticationRequestInterceptor;
import com.programmingtechie.identity_service.dto.response.Patient.PatientDetailsResponse;

@FeignClient(
        name = "patient-client",
        url = "http://localhost:8080/api/v1/patient",
        configuration = {AuthenticationRequestInterceptor.class})
public interface PatientClient {

    @GetMapping(value = "/customer/{customerId}", produces = MediaType.APPLICATION_JSON_VALUE)
    PatientDetailsResponse getByCustomerId(@PathVariable("customerId") String customerId);
}
