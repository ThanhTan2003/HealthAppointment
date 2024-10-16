package com.programmingtechie.identity_service.controller;

import java.text.ParseException;

import org.springframework.web.bind.annotation.CrossOrigin;
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

@CrossOrigin("*")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthenController {
    final AuthenService authenService;

    @PostMapping("/login")
    public AuthenResponse postMethodName(@RequestBody AuthenRequest authenRequest) {
        return authenService.creataLogin(authenRequest);
    }

    @PostMapping("/introspect")
    IntrospectResponse authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        return authenService.introspect(request);
    }

    @PostMapping("/refresh")
    AuthenResponse authenticate(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        return authenService.refreshToken(request);
    }

    @PostMapping("/logout")
    void logout(@RequestBody LogoutRequest request)
            throws ParseException, JOSEException {
        authenService.logout(request);
    }
}
