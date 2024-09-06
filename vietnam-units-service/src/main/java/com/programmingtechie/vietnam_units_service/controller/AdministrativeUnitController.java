package com.programmingtechie.vietnam_units_service.controller;

import com.programmingtechie.vietnam_units_service.model.AdministrativeUnit;
import com.programmingtechie.vietnam_units_service.service.AdministrativeUnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vietnam-units/administrative-unit")
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
