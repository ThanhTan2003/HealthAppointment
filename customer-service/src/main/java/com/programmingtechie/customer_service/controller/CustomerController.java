package com.programmingtechie.customer_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.customer_service.dto.request.CustomerRequest;
import com.programmingtechie.customer_service.dto.response.CustomerResponse;
import com.programmingtechie.customer_service.dto.response.PageResponse;
import com.programmingtechie.customer_service.service.CustomerServiceV1;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/customer")
@RequiredArgsConstructor
@CrossOrigin("*")
public class CustomerController {
    final CustomerServiceV1 customerServiceV1;

    @GetMapping("/welcome")
    public String getGreeting() {
        return "Welcome to health appointment app";
    }
    
    @PostMapping("/welcome/login")
    public String loginSystem(@RequestBody String entity) {
        return entity;
    }
    
    @PostMapping("/welcome/register")
    @ResponseStatus(HttpStatus.CREATED)
    public String createCustomer(@RequestBody CustomerRequest customerRequest)
    {
        customerServiceV1.createCustomer(customerRequest);
        return "Tạo tài khoản thành công!";
    }

    @GetMapping("/get-all")
    public PageResponse<CustomerResponse> getAll (
            @RequestParam(value = "page", required = false, defaultValue = "1") int page,
            @RequestParam(value = "size", required = false, defaultValue = "5") int size
    )
    {
        return customerServiceV1.getAllCustomer(page, size);
    }
}
