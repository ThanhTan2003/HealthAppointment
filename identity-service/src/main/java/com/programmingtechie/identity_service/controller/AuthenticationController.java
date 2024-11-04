package com.programmingtechie.identity_service.controller;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nimbusds.jose.JOSEException;
import com.programmingtechie.identity_service.dto.request.AuthenticationRequest;
import com.programmingtechie.identity_service.dto.request.Customer.CustomerAuthenticationRequest;
import com.programmingtechie.identity_service.dto.request.IntrospectRequest;
import com.programmingtechie.identity_service.dto.request.LogOutRequest;
import com.programmingtechie.identity_service.dto.request.RefreshRequest;
import com.programmingtechie.identity_service.dto.response.AuthenticationResponse;
import com.programmingtechie.identity_service.dto.response.IntrospectResponse;
import com.programmingtechie.identity_service.service.AuthenticationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.WebRequest;

@RestController
@RequestMapping("/api/v1/identity/auth")
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {

        // Tạo body của response
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false)); // đường dẫn của request

        // Trả về response với mã 500
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/log-in")
    public AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    @PostMapping("/customer/log-in")
    public AuthenticationResponse loginCustomer(@RequestBody CustomerAuthenticationRequest request) {
        return authenticationService.authenticate(request);
    }

    // Kiem tra token
    @PostMapping("/introspect")
    public IntrospectResponse authenticate(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return authenticationService.introspect(request);
    }

    @PostMapping("/log-out")
    public void logout(@RequestBody LogOutRequest request) throws ParseException, JOSEException {
        authenticationService.logOut(request);
    }

    @PostMapping("/refresh")
    public AuthenticationResponse authenticate(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return authenticationService.refreshToken(request);
    }

    @PostMapping("/customer/refresh")
    public AuthenticationResponse customerAuthenticate(@RequestBody RefreshRequest request) throws ParseException, JOSEException {
        return authenticationService.customerRefreshToken(request);
    }
}
