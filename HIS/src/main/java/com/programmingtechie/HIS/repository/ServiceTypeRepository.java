package com.programmingtechie.HIS.repository;

import com.programmingtechie.HIS.model.ServiceType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceTypeRepository extends JpaRepository<ServiceType, String> {

}
