package com.demo.hotel_management.utils;

import com.demo.hotel_management.entity.Building;
import com.demo.hotel_management.entity.Room;
import com.demo.hotel_management.repository.BuildingRepository;
import com.demo.hotel_management.repository.RoomRepository;
import com.demo.hotel_management.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

@Component
public class InitialDataIngesting {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BuildingRepository buildingRepository;


    @Autowired
    private RoomService roomService;

    @EventListener
    public void populateInitialData(ApplicationReadyEvent event) {

        roomRepository.deleteAll();
        buildingRepository.deleteAll();

        Building building1 = buildingRepository.save(new Building(1, "Будинок 1", new ArrayList<>()));
        Room room1 = roomRepository.save(new Room(1, new ArrayList<>(), building1));
        Room room2 = roomRepository.save(new Room(2, new ArrayList<>(), building1));
        Room room3 = roomRepository.save(new Room(3, new ArrayList<>(), building1));
        Room room4 = roomRepository.save(new Room(4, new ArrayList<>(), building1));
        Room room5 = roomRepository.save(new Room(5, new ArrayList<>(), building1));
        building1.setRoomList(Arrays.asList(room1, room2, room3, room4, room5));

        Building building2 = buildingRepository.save(new Building(2, "Будинок 2", new ArrayList<>()));
        Room room6 = roomRepository.save(new Room(6, new ArrayList<>(), building2));
        Room room7 = roomRepository.save(new Room(7, new ArrayList<>(), building2));
        building2.setRoomList(Arrays.asList(room6, room7));

        Building building3 = buildingRepository.save(new Building(3, "Будинок 3", new ArrayList<>()));
        Room room8 = roomRepository.save(new Room(8, new ArrayList<>(), building3));
        Room room9 = roomRepository.save(new Room(9, new ArrayList<>(), building3));
        building3.setRoomList(Arrays.asList(room8, room9));
    }
}