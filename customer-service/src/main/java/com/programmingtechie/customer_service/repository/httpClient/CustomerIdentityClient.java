package com.programmingtechie.customer_service.repository.httpClient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "identity-client", url = "http://localhost:8080/api/v1/identity/customer")
public interface CustomerIdentityClient {
    @GetMapping(value = "/id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public CustomerIdentityClient getByCustomerId(@PathVariable String id);
}
