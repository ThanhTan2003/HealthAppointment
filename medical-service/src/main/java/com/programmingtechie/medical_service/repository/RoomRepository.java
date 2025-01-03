package com.programmingtechie.medical_service.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.medical_service.model.Room;

public interface RoomRepository extends JpaRepository<Room, String> {

    @Query("SELECT r FROM Room r order by id asc")
    Page<Room> getAllRooms(Pageable pageable);

    @Query("SELECT DISTINCT r.function FROM Room r")
    List<String> findDistinctFunctions();

    // lấy danh sách các phòng còn trống trong một khoảng thời gian cụ thể
    @Query(
            value = "SELECT * FROM room r WHERE "
                    + "unaccent(LOWER(r.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(r.location)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(r.function)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(r.status)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    Page<Room> searchRooms(@Param("keyword") String keyword, Pageable pageable);

    @Query(
            value = "SELECT * FROM Room r WHERE NOT EXISTS (" + "SELECT 1 "
                    + "FROM service_time_frame stf "
                    + "WHERE stf.room_id = r.id "
                    + "AND stf.is_active = TRUE "
                    + "AND stf.day_of_week = :dayOfWeek "
                    + "AND stf.time_frame_id = :timeFrameId  "
                    + ") "
                    + "AND unaccent(LOWER(r.status)) LIKE unaccent(LOWER('Đang hoạt động')) "
                    + "AND ("
                    + "unaccent(LOWER(r.id)) LIKE unaccent(LOWER(:keyword)) OR "
                    + "unaccent(LOWER(r.name)) LIKE unaccent(LOWER(:keyword)) "
                    + ") "
                    + "AND unaccent(LOWER(r.function)) LIKE unaccent(LOWER(:function)) "
                    + "ORDER BY r.function ASC, r.name ASC",
            nativeQuery = true)
    Page<Room> getListOfAvailableRooms(
            @Param("dayOfWeek") String dayOfWeek,
            @Param("timeFrameId") String timeFrameId,
            @Param("function") String function,
            @Param("keyword") String keyword,
            Pageable pageable);

    @Query(
            value = "SELECT * FROM Room r " + "WHERE NOT EXISTS ("
                    + "    SELECT 1 "
                    + "    FROM service_time_frame stf "
                    + "    WHERE stf.room_id = r.id "
                    + "    AND stf.is_active = TRUE "
                    + "    AND stf.day_of_week = :dayOfWeek "
                    + "    AND stf.time_frame_id = :timeFrameId "
                    + ") "
                    + "AND unaccent(LOWER(r.status)) LIKE unaccent(LOWER('Đang hoạt động')) "
                    + "AND (unaccent(LOWER(r.id)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "     unaccent(LOWER(r.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))) "
                    + "AND unaccent(LOWER(r.function)) LIKE unaccent(LOWER(CONCAT('%', :function, '%'))) "
                    + "OR (r.id = :roomId) "
                    + "ORDER BY r.function ASC, r.name ASC", // Bao quanh phần OR này bằng dấu ngoặc đơn
            nativeQuery = true)
    Page<Room> getRoomsWithInUse(
            @Param("roomId") String roomId,
            @Param("dayOfWeek") String dayOfWeek,
            @Param("timeFrameId") String timeFrameId,
            @Param("function") String function,
            @Param("keyword") String keyword,
            Pageable pageable);
}
