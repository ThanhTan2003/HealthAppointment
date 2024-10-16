package com.programmingtechie.identity_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.identity_service.dto.request.UserRequest;
import com.programmingtechie.identity_service.dto.response.UserResponse;
import com.programmingtechie.identity_service.service.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@CrossOrigin("*")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createCustomer(@RequestBody UserRequest customerRequest) {
        return userService.createCustomer(customerRequest);
    }

    @GetMapping("/greeting")
    public String getMethodName(@RequestBody String entity) {
        return "hello";
    }

    @GetMapping("/get-user/{email}")
    @PostAuthorize("hasRole('ADMIN') or returnObject.result.email == authentication.name")
    public UserResponse getAuthentication(@PathVariable("email") String email) {
        var access = SecurityContextHolder.getContext().getAuthentication();
        log.info("Username: {}", access.getName());
        access.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));
        return userService.getUser(email);
    }

    @GetMapping("/get-info")
    @PostAuthorize("returnObject.email == authentication.name")
    @ResponseStatus(HttpStatus.OK)
    UserResponse getInfo(){
        return userService.getUserInfo();
    }

}
