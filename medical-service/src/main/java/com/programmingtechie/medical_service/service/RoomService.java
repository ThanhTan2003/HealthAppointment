package com.programmingtechie.medical_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.programmingtechie.medical_service.dto.request.Room.RoomAvailabilityRequest;
import com.programmingtechie.medical_service.dto.request.Room.RoomRequest;
import com.programmingtechie.medical_service.dto.response.PageResponse;
import com.programmingtechie.medical_service.dto.response.RoomResponse;
import com.programmingtechie.medical_service.mapper.RoomMapper;
import com.programmingtechie.medical_service.model.Room;
import com.programmingtechie.medical_service.repository.RoomRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoomService {
    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    public PageResponse<RoomResponse> getAllRooms(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Room> pageData = roomRepository.getAllRooms(pageable);
        List<RoomResponse> roomResponses =
                pageData.getContent().stream().map(this::mapToRoomResponse).collect(Collectors.toList());

        return PageResponse.<RoomResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(roomResponses)
                .build();
    }

    public RoomResponse getRoomById(String id) {
        Room room = roomRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng với mã: " + id));
        return mapToRoomResponse(room);
    }

    public RoomResponse createRoom(RoomRequest roomRequest) {
        Room room = Room.builder()
                .id(roomRequest.getId())
                .name(roomRequest.getName())
                .location(roomRequest.getLocation())
                .function(roomRequest.getFunction())
                .status(roomRequest.getStatus())
                .build();

        room = roomRepository.save(room);
        return mapToRoomResponse(room);
    }

    public RoomResponse updateRoom(String id, RoomRequest roomRequest) {
        Room room = roomRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng với mã: " + id));

        room.setName(roomRequest.getName());
        room.setLocation(roomRequest.getLocation());
        room.setFunction(roomRequest.getFunction());
        room.setStatus(roomRequest.getStatus());

        room = roomRepository.save(room);
        return mapToRoomResponse(room);
    }

    public void deleteRoom(String id) {
        Room room = roomRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy phòng với mã: " + id));
        roomRepository.delete(room);
    }

    public PageResponse<RoomResponse> searchRooms(String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Room> pageData = roomRepository.searchRooms(keyword, pageable);
        List<RoomResponse> roomResponses =
                pageData.getContent().stream().map(this::mapToRoomResponse).collect(Collectors.toList());

        return PageResponse.<RoomResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(roomResponses)
                .build();
    }

    private RoomResponse mapToRoomResponse(Room room) {
        return RoomResponse.builder()
                .id(room.getId())
                .name(room.getName())
                .location(room.getLocation())
                .function(room.getFunction())
                .status(room.getStatus())
                .build();
    }

    // Lay danh sach phong con trong
    public PageResponse<RoomResponse> getListOfAvailableRooms(RoomAvailabilityRequest request, int page, int size) {
        String dayOfWeek = request.getDayOfWeek();
        String timeFrameId = request.getTimeFrameId();
        String keyword = "%" + request.getKeyword() + "%";
        String function = "%" + request.getFunction() + "%";

        Pageable pageable = PageRequest.of(page - 1, size);

        Page<Room> pageData =
                roomRepository.getListOfAvailableRooms(dayOfWeek, timeFrameId, function, keyword, pageable);

        List<RoomResponse> roomResponses =
                pageData.getContent().stream().map(roomMapper::toRoomResponse).toList();

        return PageResponse.<RoomResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(roomResponses)
                .build();
    }

    public List<String> getDistinctFunctions() {
        return roomRepository.findDistinctFunctions();
    }

    public PageResponse<RoomResponse> getRoomsWithInUse(
            String roomId, String dayOfWeek, String timeFrameId, String function, String keyword, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);

        // Gọi truy vấn mới từ repository
        Page<Room> pageData = roomRepository.getRoomsWithInUse(
                roomId, dayOfWeek, timeFrameId, function, "%" + keyword + "%", pageable);

        List<RoomResponse> roomResponses = pageData.getContent().stream()
                .map(room -> RoomResponse.builder()
                        .id(room.getId())
                        .name(room.getName())
                        .location(room.getLocation())
                        .function(room.getFunction())
                        .status(room.getStatus())
                        .build())
                .collect(Collectors.toList());

        return PageResponse.<RoomResponse>builder()
                .currentPage(page)
                .pageSize(pageData.getSize())
                .totalPages(pageData.getTotalPages())
                .totalElements(pageData.getTotalElements())
                .data(roomResponses)
                .build();
    }
}
