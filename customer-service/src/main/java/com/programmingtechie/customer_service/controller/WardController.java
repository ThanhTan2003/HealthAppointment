package com.programmingtechie.customer_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.customer_service.model.Ward;
import com.programmingtechie.customer_service.service.WardService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/customer/vietnam-units/ward")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
public class WardController {
    final WardService wardService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createWard(@RequestBody Ward ward) {
        wardService.createWard(ward);
        return "Created new ward successfully!";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Ward> getAllWards() {
        return wardService.getAllWards();
    }

    @GetMapping("/district-code/{code}")
    public List<Ward> getByDistrict_Code(@PathVariable String code) {
        return wardService.getByDistrict_Code(code);
    }
}
