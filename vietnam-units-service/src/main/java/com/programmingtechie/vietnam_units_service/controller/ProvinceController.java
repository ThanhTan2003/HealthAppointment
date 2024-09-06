package com.programmingtechie.vietnam_units_service.controller;

import com.programmingtechie.vietnam_units_service.model.Province;
import com.programmingtechie.vietnam_units_service.service.ProvinceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vietnam-units/province")
@RequiredArgsConstructor
@Slf4j
public class ProvinceController {
    final ProvinceService provinceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createProvince(@RequestBody Province province) {
        provinceService.createProvince(province);
        return "Created new province successfully!";
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Province> getAllProvinces() {
        return provinceService.getAllProvinces();
    }
}
