package com.programmingtechie.vietnam_units_service.controller;

import com.programmingtechie.vietnam_units_service.model.AdministrativeRegion;
import com.programmingtechie.vietnam_units_service.service.AdministrativeRegionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vietnam-units/administrative-region")
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
