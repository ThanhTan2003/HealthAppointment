package com.programmingtechie.vietnam_units_service.service;

import com.programmingtechie.vietnam_units_service.model.Province;
import com.programmingtechie.vietnam_units_service.repository.ProvinceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProvinceService {
    final ProvinceRepository provinceRepository;

    public void createProvince(Province province) {
        provinceRepository.save(province);
    }

    public List<Province> getAllProvinces() {
        return provinceRepository.findAll();
    }
}
