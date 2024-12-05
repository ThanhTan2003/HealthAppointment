package com.programmingtechie.appointment_service.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;

import com.programmingtechie.appointment_service.config.AuthenticationRequestInterceptor;

@FeignClient(
        name = "vnpay-client",
        url = "${app.vnpay-payment}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface VNPayClient {}
