package com.programmingtechie.customer_service.controller.vietnam_units;


import com.programmingtechie.customer_service.model.vietnam_units.AdministrativeUnit;
import com.programmingtechie.customer_service.service.vietnam_units.AdministrativeUnitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
