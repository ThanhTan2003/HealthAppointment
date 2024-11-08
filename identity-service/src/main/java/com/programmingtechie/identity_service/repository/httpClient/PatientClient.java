package com.programmingtechie.identity_service.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "patient-client", url = "http://localhost/api/v1/patient")
public interface PatientClient {}
