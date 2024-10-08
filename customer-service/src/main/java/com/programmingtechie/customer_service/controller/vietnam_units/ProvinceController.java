package com.programmingtechie.customer_service.controller.vietnam_units;


import com.programmingtechie.customer_service.model.vietnam_units.Province;
import com.programmingtechie.customer_service.service.vietnam_units.ProvinceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/customer/vietnam-units/province")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin
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
