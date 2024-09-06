package com.programmingtechie.vietnam_units_service.controller;

import com.programmingtechie.vietnam_units_service.model.District;
import com.programmingtechie.vietnam_units_service.service.DistrictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vietnam-units/district")
@RequiredArgsConstructor
@Slf4j
public class DistrictController {
    final DistrictService districtService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createDistrict(@RequestBody District district) {
        districtService.createDistrict(district);
        return "Created new district successfully!";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<District> getAllDistricts() {
        return districtService.getAllDistricts();
    }
}
