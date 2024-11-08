package com.programmingtechie.identity_service.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;

import com.programmingtechie.identity_service.config.AuthenticationRequestInterceptor;

@FeignClient(
        name = "patient-client",
        url = "http://localhost/api/v1/patient",
        configuration = {AuthenticationRequestInterceptor.class})
public interface PatientClient {}
