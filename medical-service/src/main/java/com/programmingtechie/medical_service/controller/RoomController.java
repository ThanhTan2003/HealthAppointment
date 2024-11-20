package com.programmingtechie.medical_service.controller;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import com.programmingtechie.medical_service.dto.request.Room.RoomAvailabilityRequest;
import com.programmingtechie.medical_service.dto.request.Room.RoomRequest;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.dto.response.RoomResponse;
import com.programmingtechie.medical_service.service.RoomService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/medical/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {

        // Tạo body của response
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", "Internal Server Error");
        body.put("message", ex.getMessage());
        body.put("path", request.getDescription(false)); // đường dẫn của request

        // Trả về response với mã 500
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/get-all")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<RoomResponse>> getAllRooms(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(roomService.getAllRooms(page, size));
    }

    @GetMapping("/get-functions")
    @PreAuthorize("" + "hasRole('QuanLyLichKhamBenh') or " + "hasRole('GiamDoc')")
    public ResponseEntity<List<String>> getRoomFunctions() {
        List<String> functions = roomService.getDistinctFunctions();
        return ResponseEntity.ok(functions);
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable String id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.createRoom(roomRequest));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable String id, @RequestBody RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.updateRoom(id, roomRequest));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("" + "hasRole('QuanTriVienHeThong') or " + "hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<RoomResponse>> searchRooms(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(roomService.searchRooms(keyword, page, size));
    }

    @GetMapping("/get-list-of-available-rooms")
    @PreAuthorize("hasRole('GiamDoc') or hasRole('QuanLyLichKhamBenh')")
    public ResponseEntity<PageResponse<RoomResponse>> getListOfAvailableRooms(
            @ModelAttribute RoomAvailabilityRequest request,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        return ResponseEntity.ok(roomService.getListOfAvailableRooms(request, page, size));
    }

    @GetMapping("/get-rooms-and-update")
    @PreAuthorize("hasRole('GiamDoc') or hasRole('QuanLyLichKhamBenh')")
    public ResponseEntity<PageResponse<RoomResponse>> getRoomsAndUpdate(
            @RequestParam String roomId,
            @RequestParam String dayOfWeek,
            @RequestParam Integer startTime,
            @RequestParam Integer endTime,
            @RequestParam String function,
            @RequestParam String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        // Gọi service để lấy danh sách phòng còn trống và thêm phòng đang sử dụng
        PageResponse<RoomResponse> availableRooms =
                roomService.getRoomsWithInUse(roomId, dayOfWeek, startTime, endTime, function, keyword, page, size);

        // Trả về danh sách phòng
        return ResponseEntity.ok(availableRooms);
    }
}

//    // lấy danh sách các phòng có sẵn
//    @GetMapping("/get-list-of-available-rooms")
//    @PreAuthorize(""
//            + "hasRole('GiamDoc') or "
//            + "hasRole('QuanLyLichKhamBenh')")
//    public ResponseEntity<PageResponse<ServiceResponse>> getListOfAvailableRooms(
//            @RequestParam("day-of-week") Integer dayOfWeek,
//            @RequestParam("start-time") Integer startTime,
//            @RequestParam("end-time") Integer endTime,
//            @RequestParam("function") String function,
//            @RequestParam("keyword") String keyword,
//            @RequestParam(value = "page", defaultValue = "1") int page,
//            @RequestParam(value = "size", defaultValue = "10") int size) {
//        return ResponseEntity.ok(roomService.getListOfAvailableRooms(page, size));
//    }
