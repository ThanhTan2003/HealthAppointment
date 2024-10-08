package com.programmingtechie.customer_service.controller.vietnam_units;

import com.programmingtechie.customer_service.model.vietnam_units.District;
import com.programmingtechie.customer_service.service.vietnam_units.DistrictService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
