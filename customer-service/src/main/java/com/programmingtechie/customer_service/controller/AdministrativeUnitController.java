package com.programmingtechie.customer_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.programmingtechie.customer_service.model.AdministrativeUnit;
import com.programmingtechie.customer_service.service.AdministrativeUnitService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/customer/vietnam-units/administrative-unit")
@RequiredArgsConstructor
@Slf4j
public class AdministrativeUnitController {
    final AdministrativeUnitService administrativeUnitService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createUnit(@RequestBody AdministrativeUnit unit) {
        administrativeUnitService.createUnit(unit);
        return "Created new administrative unit successfully!";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AdministrativeUnit> getAllUnits() {
        return administrativeUnitService.getAllUnits();
    }
}
