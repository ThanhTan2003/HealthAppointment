package com.programmingtechie.customer_service.controller.vietnam_units;


import com.programmingtechie.customer_service.model.vietnam_units.Ward;
import com.programmingtechie.customer_service.service.vietnam_units.WardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer/vietnam-units/ward")
@RequiredArgsConstructor
@Slf4j
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
