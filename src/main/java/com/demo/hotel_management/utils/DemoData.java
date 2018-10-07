package com.demo.hotel_management.utils;

import com.demo.hotel_management.entity.*;
import com.demo.hotel_management.repository.BuildingRepository;
import com.demo.hotel_management.repository.ClientRepository;
import com.demo.hotel_management.repository.RoomRepository;
import com.demo.hotel_management.repository.VacationRepository;
import com.demo.hotel_management.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.StreamSupport;

@Component
public class DemoData {
    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private BuildingRepository buildingRepository;

    @Autowired
    private RoomService roomService;

    @Autowired
    private ClientRepository clientRepository;

    @EventListener
    public void appReady(ApplicationReadyEvent event) {

//        Map<Room.BedType, Integer> twoSingleBeds = new HashMap<>();
//        twoSingleBeds.put(Room.BedType.SINGLE_BED, 2);
//
//        Map<Room.BedType, Integer> oneDoubleOneSingleBeds = new HashMap<>();
//        twoSingleBeds.put(Room.BedType.SINGLE_BED, 1);
//        twoSingleBeds.put(Room.BedType.DOUBLE_BED, 1);
//
//        Map<Room.BedType, Integer> oneDoubleBeds = new HashMap<>();
//        twoSingleBeds.put(Room.BedType.DOUBLE_BED, 1);


        Building building1 = new Building(1, "Будинок 1", new ArrayList<>());
        building1 = buildingRepository.save(building1);

        Room room1 = new Room(1,
//                twoSingleBeds,
                new ArrayList<>(), building1);
        room1 = roomRepository.save(room1);
        Room room2 = new Room(2,
//                twoSingleBeds,
                new ArrayList<>(), building1);
        room2 = roomRepository.save(room2);
        Room room3 = new Room(3,
//                twoSingleBeds,
                new ArrayList<>(), building1);
        room3 = roomRepository.save(room3);
        Room room4 = new Room(4,
//                twoSingleBeds,
                new ArrayList<>(), building1);
        room4 = roomRepository.save(room4);
        Room room5 = new Room(5,
//                oneDoubleOneSingleBeds,
                new ArrayList<>(), building1);
        room5 = roomRepository.save(room5);

        building1.setRoomList(Arrays.asList(room1, room2, room3, room4, room5));


        Building building2 = new Building(2, "Будинок 2", new ArrayList<>());
        building2 = buildingRepository.save(building2);
        Room room6 = new Room(6,
//                twoSingleBeds,
                new ArrayList<>(), building2);
        room6 = roomRepository.save(room6);
        Room room7 = new Room(7,
//                twoSingleBeds,
                new ArrayList<>(), building2);
        room7 = roomRepository.save(room7);

        building2.setRoomList(Arrays.asList(room6, room7));


        Building building3 = new Building(3, "Будинок 3", new ArrayList<>());
        building3 = buildingRepository.save(building3);
        Room room8 = new Room(8,
//                twoSingleBeds,
                new ArrayList<>(), building3);
        room8 = roomRepository.save(room8);
        Room room9 = new Room(9,
//                twoSingleBeds,
                new ArrayList<>(), building3);
        room9 = roomRepository.save(room9);

        building3.setRoomList(Arrays.asList(room8, room9));

        Vacation vacation = new Vacation();

        RoomVacation roomVacation1 = new RoomVacation();
//        roomVacation1.setOccupiedBeds(new HashMap<>());
        roomVacation1.setRoom(room2);
        roomVacation1.setVacation(vacation);
//        RoomVacation roomVacation2 = new RoomVacation();
//        roomVacation2.setOccupiedBeds(new HashMap<>());
//        roomVacation2.setRoom(room3);
//        roomVacation2.setVacation(vacation);
        List<RoomVacation> roomVacationList = new ArrayList<>(Collections.singletonList(roomVacation1));
        vacation.setRoomVacationList(roomVacationList);

        Vacation.CustomDate customDate = new Vacation.CustomDate();
        customDate.setArrivalDate(LocalDate.now().minusDays(15));
        customDate.setArrivalDayPart(Vacation.DayPart.MORNING);
        customDate.setLeaveDate(LocalDate.now().minusDays(2));
        customDate.setLeaveDayPart(Vacation.DayPart.EVENING);
        vacation.setVacationDate(customDate);

        vacation.setResidentsCount(4);

        Client client = new Client();
        client.setName("ClientName");
        client.setEmail("test@gmail.com");
        client.setOtherClientInfo("other info");
        client.setPhoneNumber("0937457255");
        vacation.setClient(client);
        vacation.setWithChildren(false);

        vacationRepository.save(vacation);

        ////////////////////////////////////


        Vacation vacation2 = new Vacation();

        RoomVacation roomVacation12 = new RoomVacation();
//        roomVacation12.setOccupiedBeds(new HashMap<>());
        roomVacation12.setRoom(room2);
        roomVacation12.setVacation(vacation2);
        RoomVacation roomVacation22 = new RoomVacation();
//        roomVacation22.setOccupiedBeds(new HashMap<>());
        roomVacation22.setRoom(room3);
        roomVacation22.setVacation(vacation2);
        List<RoomVacation> roomVacationList2 = new ArrayList<>(Arrays.asList(roomVacation12, roomVacation22));
        vacation2.setRoomVacationList(roomVacationList2);

        Vacation.CustomDate customDate2 = new Vacation.CustomDate();
        customDate2.setArrivalDate(LocalDate.now().minusDays(25));
        customDate2.setArrivalDayPart(Vacation.DayPart.MORNING);
        customDate2.setLeaveDate(LocalDate.now());
        customDate2.setLeaveDayPart(Vacation.DayPart.MORNING);
        vacation2.setVacationDate(customDate2);

        vacation2.setResidentsCount(6);

        Client client2 = new Client();
        client2.setName("ClientName2");
        client2.setEmail("test@gmail.com2");
        client2.setOtherClientInfo("other info2");
        client2.setPhoneNumber("0937457256");
        vacation2.setClient(client2);
        vacation2.setWithChildren(true);

        vacationRepository.save(vacation2);
    }
}