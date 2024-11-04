package com.programmingtechie.medical_service.controller;

import com.programmingtechie.medical_service.dto.request.RoomRequest;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.dto.response.RoomResponse;
import com.programmingtechie.medical_service.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/medical/room")
@RequiredArgsConstructor
public class RoomController {
    private final RoomService roomService;

    @GetMapping("/get-all")
    @PreAuthorize("" +
            "hasRole('QuanTriVienHeThong') or " +
            "hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<RoomResponse>> getAllRooms(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(roomService.getAllRooms(page, size));
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("" +
            "hasRole('QuanTriVienHeThong') or " +
            "hasRole('GiamDoc')")
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable String id) {
        return ResponseEntity.ok(roomService.getRoomById(id));
    }

    @PostMapping("/create")
    @PreAuthorize("" +
            "hasRole('QuanTriVienHeThong') or " +
            "hasRole('GiamDoc')")
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.createRoom(roomRequest));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("" +
            "hasRole('QuanTriVienHeThong') or " +
            "hasRole('GiamDoc')")
    public ResponseEntity<RoomResponse> updateRoom(
            @PathVariable String id,
            @RequestBody RoomRequest roomRequest) {
        return ResponseEntity.ok(roomService.updateRoom(id, roomRequest));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("" +
            "hasRole('QuanTriVienHeThong') or " +
            "hasRole('GiamDoc')")
    public ResponseEntity<Void> deleteRoom(@PathVariable String id) {
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @PreAuthorize("" +
            "hasRole('QuanTriVienHeThong') or " +
            "hasRole('GiamDoc')")
    public ResponseEntity<PageResponse<RoomResponse>> searchRooms(
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        return ResponseEntity.ok(roomService.searchRooms(keyword, page, size));
    }
}
