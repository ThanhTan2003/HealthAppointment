package com.programmingtechie.identity_service.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.programmingtechie.identity_service.dto.request.AuthenRequest;
import com.programmingtechie.identity_service.dto.request.IntrospectRequest;
import com.programmingtechie.identity_service.dto.request.LogoutRequest;
import com.programmingtechie.identity_service.dto.request.RefreshRequest;
import com.programmingtechie.identity_service.dto.response.AuthenResponse;
import com.programmingtechie.identity_service.dto.response.IntrospectResponse;
import com.programmingtechie.identity_service.service.AuthenService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/identity/auth")
@RequiredArgsConstructor
public class AuthenController {
    final AuthenService authenService;

    @PostMapping("/log-in")
    public AuthenResponse authenticateUser(@RequestBody AuthenRequest request) {
        return authenService.authenticate(request);
    }

    @PostMapping("/customer/log-in")
    public AuthenResponse authenticateCustomer(@RequestBody AuthenRequest authenRequest) {
        return authenService.authenticatedCustomer(authenRequest);
    }

    @PostMapping("/introspect")
    IntrospectResponse introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return authenService.introspect(request);
    }

    @PostMapping("/customer/refresh")
    AuthenResponse refreshToken(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return authenService.refreshToken(request);
    }

    @PostMapping("/log-out")
    void logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenService.logout(request);
    }
}
