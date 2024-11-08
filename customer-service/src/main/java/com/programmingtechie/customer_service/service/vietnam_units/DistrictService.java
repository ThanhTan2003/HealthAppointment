package com.programmingtechie.customer_service.service.vietnam_units;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.customer_service.model.vietnam_units.District;
import com.programmingtechie.customer_service.repository.vietnam_units.DistrictRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    public List<District> getByProvince_Code(String code) {
        return districtRepository.findByProvinceCode(code);
    }
}
