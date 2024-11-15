package com.programmingtechie.customer_service.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.programmingtechie.customer_service.config.AuthenticationRequestInterceptor;
import com.programmingtechie.customer_service.dto.response.CustomerIdentityResponse;
import com.programmingtechie.customer_service.dto.response.CustomerWithPatientDetailsResponse;

@FeignClient(
        name = "identity-client",
        url = "http://localhost:8080/api/v1/identity/customer",
        configuration = {AuthenticationRequestInterceptor.class})
public interface CustomerIdentityClient {
    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerIdentityResponse getByCustomerId(@PathVariable String id);

    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerWithPatientDetailsResponse getCustomerWithPatientDetails(@PathVariable String customerId);

    @GetMapping(value = "/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerIdentityResponse getByEmail(@PathVariable String email);

    @GetMapping(value = "/phoneNumber/{phoneNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerIdentityResponse getByPhoneNumber(@PathVariable String phoneNumber);
}