package com.programmingtechie.identity_service.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.programmingtechie.identity_service.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUserName(String userName);

    Optional<User> findByUserName(String userName);

    @Query("SELECT u FROM User u")
    Page<User> getAllUser(Pageable pageable);

//    // Tìm kiếm dịch vụ theo từ khoá với phân trang
//    @Query(value = "SELECT * FROM User s WHERE " +
//            "unaccent(LOWER(s.user_name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
//            "unaccent(LOWER(s.doctor_id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
//            "unaccent(LOWER(s.status)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
//            "unaccent(LOWER(s.account_type_id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))",
//            nativeQuery = true)
//    Page<User> searchServices(@Param("keyword") String keyword, Pageable pageable);

    @Query(value = "SELECT * FROM account s WHERE " +
            "unaccent(LOWER(s.user_name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "unaccent(LOWER(s.doctor_id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "unaccent(LOWER(s.status)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR " +
            "unaccent(LOWER(s.account_type_id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    Page<User> searchServices(@Param("keyword") String keyword, Pageable pageable);

}
