package com.programmingtechie.identity_service.repository;

import com.programmingtechie.identity_service.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {

}
