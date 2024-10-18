package com.programmingtechie.api_gateway.repository;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import com.programmingtechie.api_gateway.dto.request.IntrospectRequest;
import com.programmingtechie.api_gateway.dto.response.IntrospectResponse;

import reactor.core.publisher.Mono;

public interface IdentityClient {
    @PostExchange(url = "/auth/introspect", contentType = MediaType.APPLICATION_JSON_VALUE)
    Mono<IntrospectResponse> introspect(@RequestBody IntrospectRequest request);
}