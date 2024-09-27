package com.programmingtechie.customer_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.customer_service.model.AdministrativeRegion;
import com.programmingtechie.customer_service.service.AdministrativeRegionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/customer/vietnam-units/administrative-region")
@RequiredArgsConstructor
@Slf4j
public class AdministrativeRegionController {
    final AdministrativeRegionService administrativeRegionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createRegion(@RequestBody AdministrativeRegion region) {
        administrativeRegionService.createRegion(region);
        return "Created new administrative region successfully!";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AdministrativeRegion> getAllRegions() {
        return administrativeRegionService.getAllRegions();
    }
}
