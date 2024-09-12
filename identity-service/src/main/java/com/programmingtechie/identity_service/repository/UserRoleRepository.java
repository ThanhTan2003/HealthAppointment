package com.programmingtechie.identity_service.repository;

import com.programmingtechie.identity_service.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, String> {
}
