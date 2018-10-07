package com.demo.hotel_management.utils;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.Room;
import com.demo.hotel_management.entity.RoomVacation;
import com.demo.hotel_management.entity.Vacation;
import com.demo.hotel_management.repository.ClientRepository;
import com.demo.hotel_management.repository.RoomRepository;
import com.demo.hotel_management.repository.RoomVacationRepository;
import com.demo.hotel_management.repository.VacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

//todo change repositories with service layer
@Component
public class EntityDtoConverter {

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private RoomVacationRepository roomVacationRepository;

    public VacationDto convertVacationEntityToDto(Vacation vacation) {
        VacationDto vacationDto = new VacationDto();
        vacationDto.setVacationId(vacation.getId());
        vacationDto.setInactive(vacation.getInactive());

        vacationDto.setArrivalDate(vacation.getVacationDate().getArrivalDate());
        vacationDto.setArrivalDayPart(vacation.getVacationDate().getArrivalDayPart().getDayPartNumber());

        vacationDto.setLeaveDate(vacation.getVacationDate().getLeaveDate());
        vacationDto.setLeaveDayPart(vacation.getVacationDate().getLeaveDayPart().getDayPartNumber());

        vacationDto.setClientId(vacation.getClient().getId());
        vacationDto.setClientName(vacation.getClient().getName());
        vacationDto.setClientOtherInfo(vacation.getClient().getOtherClientInfo());
        vacationDto.setClientPhoneNumber(vacation.getClient().getPhoneNumber());

        vacationDto.setResidentsCount(vacation.getResidentsCount());

        Set<Integer> roomNumbers = vacation.getRoomVacationList().stream()
                .map(e -> e.getRoom().getRoomNumber())
                .collect(Collectors.toSet());

        vacationDto.setRoomNumbers(roomNumbers);
        return vacationDto;
    }

    public Vacation convertVacationDtoToEntity(VacationDto vacationDto) {
        Vacation vacation = new Vacation();
        if (vacationDto.getVacationId() != null) {
            vacation.setId(vacationDto.getVacationId());
            roomVacationRepository.findByVacationId(vacation.getId())
                    .forEach(e -> roomVacationRepository.delete(e));
        }

        List<RoomVacation> roomVacations = new ArrayList<>();
        List<Room> rooms = roomRepository.findByRoomNumberIn(vacationDto.getRoomNumbers());

        rooms.forEach(e -> {
            RoomVacation roomVacation = new RoomVacation();
            roomVacation.setRoom(e);
            roomVacation.setVacation(vacation);
            // roomVacation.setOccupiedBeds(); todo
            roomVacations.add(roomVacation);
        });

        vacation.setRoomVacationList(roomVacations);


        vacation.setClient(clientRepository
                .findById(vacationDto.getClientId())
                .orElseThrow(NoSuchElementException::new));

        vacation.setResidentsCount(vacationDto.getResidentsCount());
        vacation.setInactive(vacationDto.getInactive());

        Vacation.CustomDate customDate = new Vacation.CustomDate();
        customDate.setArrivalDate(vacationDto.getArrivalDate());
        customDate.setArrivalDayPart(Vacation.DayPart.getDayPartByNumber(vacationDto.getArrivalDayPart()));
        customDate.setLeaveDate(vacationDto.getLeaveDate());
        customDate.setLeaveDayPart(Vacation.DayPart.getDayPartByNumber(vacationDto.getLeaveDayPart()));
        vacation.setVacationDate(customDate);

        return vacation;
    }
}
