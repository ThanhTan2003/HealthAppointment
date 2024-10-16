package com.programmingtechie.customer_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.programmingtechie.customer_service.model.Province;
import com.programmingtechie.customer_service.repository.ProvinceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
