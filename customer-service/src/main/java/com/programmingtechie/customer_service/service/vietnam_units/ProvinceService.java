package com.programmingtechie.customer_service.service.vietnam_units;

import com.programmingtechie.customer_service.model.vietnam_units.Province;
import com.programmingtechie.customer_service.repository.vietnam_units.ProvinceRepository;
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
