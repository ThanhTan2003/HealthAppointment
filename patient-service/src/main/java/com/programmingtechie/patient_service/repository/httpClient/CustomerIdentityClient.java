package com.programmingtechie.patient_service.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.programmingtechie.patient_service.config.AuthenticationRequestInterceptor;
import com.programmingtechie.patient_service.dto.response.customer.CustomerIdentityResponse;
import com.programmingtechie.patient_service.dto.response.customer.CustomerWithPatientDetailsResponse;

@FeignClient(
        name = "identity-client",
        url = "${app.services.identity}",
        configuration = {AuthenticationRequestInterceptor.class})
public interface CustomerIdentityClient {
    @GetMapping(value = "/customer/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerIdentityResponse getByCustomerId(@PathVariable String id);

    @GetMapping(value = "/customer/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerWithPatientDetailsResponse getCustomerWithPatientDetails(@PathVariable String customerId);

    @GetMapping(value = "/customer/email/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerIdentityResponse getByEmail(@PathVariable String email);

    @GetMapping(value = "/customer/phoneNumber/{phoneNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerIdentityResponse getByPhoneNumber(@PathVariable String phoneNumber);
}
