package com.programmingtechie.customer_service.repository.vietnam_units;


import com.programmingtechie.customer_service.model.vietnam_units.Ward;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WardRepository extends JpaRepository<Ward, String> {
    List<Ward> findByDistrictCode(String code);
}
