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

import com.programmingtechie.customer_service.model.District;
import com.programmingtechie.customer_service.service.DistrictService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1/customer/vietnam-units/district")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
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

    @GetMapping("/province-code/{code}")
    public List<District> getByProvince_Code(@PathVariable String code) {
        return districtService.getByProvince_Code(code);
    }
}
