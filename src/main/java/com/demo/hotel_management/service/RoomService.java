package com.demo.hotel_management.service;

import com.demo.hotel_management.entity.Room;
import com.demo.hotel_management.repository.BuildingRepository;
import com.demo.hotel_management.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class RoomService {

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private RoomRepository roomRepository;

    public Room save(Room room) {

        return roomRepository.save(room);
    }
}

