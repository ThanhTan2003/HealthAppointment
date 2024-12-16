package com.programmingtechie.HIS.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.programmingtechie.HIS.model.Room;

public interface RoomRepository extends JpaRepository<Room, String> {}
