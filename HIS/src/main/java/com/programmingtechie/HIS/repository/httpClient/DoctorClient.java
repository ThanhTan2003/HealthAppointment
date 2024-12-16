package com.programmingtechie.HIS.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "doctor-client", url = "${app.services.doctor}")
public interface DoctorClient {}
