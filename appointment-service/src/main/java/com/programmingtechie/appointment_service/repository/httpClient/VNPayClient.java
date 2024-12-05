package com.programmingtechie.appointment_service.repository.httpClient;

import com.programmingtechie.appointment_service.config.AuthenticationRequestInterceptor;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(
        name = "vnpay-client",
        url = "${app.vnpay-payment}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface VNPayClient {
}
