package com.programmingtechie.identity_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.programmingtechie.identity_service.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {}
