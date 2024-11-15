package com.programmingtechie.medical_service.repository;

import com.programmingtechie.medical_service.dto.response.RoomResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.programmingtechie.medical_service.model.Room;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, String> {

    @Query("SELECT r FROM Room r order by id asc")
    Page<Room> getAllRooms(Pageable pageable);

    @Query("SELECT DISTINCT r.function FROM Room r")
    List<String> findDistinctFunctions();

    //lấy danh sách các phòng còn trống trong một khoảng thời gian cụ thể
    @Query(
            value = "SELECT * FROM room r WHERE "
                    + "unaccent(LOWER(r.name)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(r.location)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(r.function)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%'))) OR "
                    + "unaccent(LOWER(r.status)) LIKE unaccent(LOWER(CONCAT('%', :keyword, '%')))",
            nativeQuery = true)
    Page<Room> searchRooms(@Param("keyword") String keyword, Pageable pageable);

    @Query(value =  "SELECT * FROM Room r WHERE NOT EXISTS (" +
            "SELECT 1 " +
            "FROM service_time_frame stf " +
            "WHERE stf.room_id = r.id " +
//                "AND unaccent(LOWER(stf.status)) LIKE unaccent(LOWER('Đang hoạt động')) " +
                "AND stf.is_active = TRUE " +
                "AND stf.day_of_week = :dayOfWeek " +
                "AND stf.start_time = :startTime  " +
                "AND stf.end_time = :endTime " +
            ") " +
            "AND unaccent(LOWER(r.status)) LIKE unaccent(LOWER('Đang hoạt động')) " +
            "AND (" +
                "unaccent(LOWER(r.id)) LIKE unaccent(LOWER(:keyword)) OR " +
                "unaccent(LOWER(r.name)) LIKE unaccent(LOWER(:keyword)) " +
            ") " +
            "AND unaccent(LOWER(r.function)) LIKE unaccent(LOWER(:function))",
            nativeQuery = true)
    Page<Room> getListOfAvailableRooms(@Param("dayOfWeek") String dayOfWeek,
                                       @Param("startTime") Integer startTime,
                                       @Param("endTime") Integer endTime,
                                       @Param("function") String function,
                                       @Param("keyword") String keyword,
                                       Pageable pageable);
}
