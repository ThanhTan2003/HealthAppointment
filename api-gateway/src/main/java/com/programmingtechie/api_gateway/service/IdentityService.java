package com.programmingtechie.api_gateway.service;

import org.springframework.stereotype.Service;

import com.programmingtechie.api_gateway.dto.request.IntrospectRequest;
import com.programmingtechie.api_gateway.dto.response.IntrospectResponse;
import com.programmingtechie.api_gateway.repository.IdentityClient;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class IdentityService {
    IdentityClient identityClient;

    public Mono<IntrospectResponse> introspect(String token) {
        return identityClient.introspect(
                IntrospectRequest.builder().token(token).build());
    }
}