package com.programmingtechie.identity_service.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.JOSEException;
import com.programmingtechie.identity_service.dto.request.AuthenticationRequest;
import com.programmingtechie.identity_service.dto.request.Customer.CustomerAuthenticationRequest;
import com.programmingtechie.identity_service.dto.request.IntrospectRequest;
import com.programmingtechie.identity_service.dto.request.LogoutRequest;
import com.programmingtechie.identity_service.dto.request.RefreshRequest;
import com.programmingtechie.identity_service.dto.response.AuthenticationResponse;
import com.programmingtechie.identity_service.dto.response.IntrospectResponse;
import com.programmingtechie.identity_service.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/identity/auth")
@RequiredArgsConstructor
public class AuthenController {
    final AuthenticationService authenService;

    @PostMapping("/log-in")
    public AuthenticationResponse authenticateUser(@RequestBody AuthenticationRequest request) {
        return authenService.authenticate(request);
    }

    @PostMapping("/customer/log-in")
    public AuthenticationResponse authenticateCustomer(
            @RequestBody CustomerAuthenticationRequest customerAuthenRequest) {
        return authenService.authenticatedCustomer(customerAuthenRequest);
    }

    @PostMapping("/introspect")
    IntrospectResponse introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return authenService.introspect(request);
    }

    @PostMapping("/customer/refresh")
    AuthenticationResponse refreshToken(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return authenService.refreshToken(request);
    }

    @PostMapping("/log-out")
    void logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authenService.logout(request);
    }
}
