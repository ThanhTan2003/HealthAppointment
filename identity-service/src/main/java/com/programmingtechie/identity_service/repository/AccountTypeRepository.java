package com.programmingtechie.identity_service.repository;

import com.programmingtechie.identity_service.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountTypeRepository extends JpaRepository<AccountType, String> {
}
