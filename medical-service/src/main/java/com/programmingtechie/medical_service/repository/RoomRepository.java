package com.programmingtechie.medical_service.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.medical_service.model.Room;

public interface RoomRepository extends JpaRepository<Room, String> {

    @Query("SELECT r FROM Room r order by id asc")
    Page<Room> getAllRooms(Pageable pageable);

    @Query(
            value = "SELECT * FROM room r WHERE "
                    + "unaccent(LOWER(r.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(r.location)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(r.function)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(r.status)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    Page<Room> searchRooms(@Param("keyword") String keyword, Pageable pageable);
}
