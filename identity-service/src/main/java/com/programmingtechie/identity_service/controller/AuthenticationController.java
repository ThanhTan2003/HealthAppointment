package com.programmingtechie.identity_service.controller;

import com.nimbusds.jose.JOSEException;
import com.programmingtechie.identity_service.dto.request.AuthenticationRequest;
import com.programmingtechie.identity_service.dto.request.IntrospectRequest;
import com.programmingtechie.identity_service.dto.response.AuthenticationResponse;
import com.programmingtechie.identity_service.dto.response.IntrospectResponse;
import com.programmingtechie.identity_service.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import java.text.ParseException;

@RestController
@RequestMapping("/api/v1/identity/auth")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/log-in")
    AuthenticationResponse authenticate(@RequestBody AuthenticationRequest request){
        return authenticationService.authenticate(request);
    }

    // Kiem tra token
    @PostMapping("/introspect")
    IntrospectResponse authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        return authenticationService.introspect(request);
    }
}
