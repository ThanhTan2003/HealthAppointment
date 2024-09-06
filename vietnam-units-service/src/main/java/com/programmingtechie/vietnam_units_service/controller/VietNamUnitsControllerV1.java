package com.programmingtechie.vietnam_units_service.controller;

import com.programmingtechie.vietnam_units_service.service.VietNamUnitsServiceV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/vietnam-units")
@RequiredArgsConstructor
@Slf4j
public class VietNamUnitsControllerV1 {
    final VietNamUnitsServiceV1 vietNamUnitsServiceV1;

}
