package com.programmingtechie.vietnam_units_service.service;

import com.programmingtechie.vietnam_units_service.model.District;
import com.programmingtechie.vietnam_units_service.repository.DistrictRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DistrictService {
    final DistrictRepository districtRepository;

    public void createDistrict(District district) {
        districtRepository.save(district);
    }

    public List<District> getAllDistricts() {
        return districtRepository.findAll();
    }
}
