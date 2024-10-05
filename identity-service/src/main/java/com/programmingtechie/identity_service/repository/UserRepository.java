package com.programmingtechie.identity_service.repository;

import com.programmingtechie.identity_service.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUserName(String userName);

    Optional<User> findByUserName(String userName);

    @Query("SELECT u FROM User u")
    Page<User> getAllUser(Pageable pageable);
}
