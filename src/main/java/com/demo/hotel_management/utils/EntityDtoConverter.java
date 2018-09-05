package com.demo.hotel_management.utils;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.entity.RoomVacation;
import com.demo.hotel_management.entity.Vacation;
import com.demo.hotel_management.repository.ClientRepository;
import com.demo.hotel_management.repository.RoomRepository;
import com.demo.hotel_management.repository.VacationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

//todo change repositories with service layer
@Component
public class EntityDtoConverter {

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private ClientRepository clientRepository;

    public VacationDto convertVacationEntityToDto(Vacation vacation) {
        VacationDto vacationDto = new VacationDto();
        vacationDto.setVacationId(vacation.getId());

        vacationDto.setArrivalDate(vacation.getVacationDate().getArrivalDate());
        vacationDto.setArrivalDayPart(vacation.getVacationDate().getArrivalDayPart().getDayPartNumber());

        vacationDto.setLeaveDate(vacation.getVacationDate().getLeaveDate());
        vacationDto.setLeaveDayPart(vacation.getVacationDate().getLeaveDayPart().getDayPartNumber());

        vacationDto.setClientId(vacation.getClient().getId());
        vacationDto.setClientName(vacation.getClient().getName());
        vacationDto.setClientOtherInfo(vacation.getClient().getOtherClientInfo());
        vacationDto.setClientPhoneNumber(vacation.getClient().getPhoneNumber());

        vacationDto.setResidentsCount(vacation.getResidentsCount());

        Set<Long> roomIds = vacation.getRoomVacationSet().stream().map(e -> e.getRoom().getId()).collect(Collectors.toSet());

        vacationDto.setRoomIds(roomIds);
        return vacationDto;
    }

    public Vacation convertVacationDtoToEntity(VacationDto vacationDto) {

        Vacation vacation = new Vacation();
        vacation.setId(vacationDto.getVacationId());

        vacation.setClient(clientRepository
                .findById(vacationDto.getClientId()).orElseThrow(NoSuchElementException::new));

        vacation.setResidentsCount(vacationDto.getResidentsCount());

        Vacation.CustomDate customDate = new Vacation.CustomDate();
        customDate.setArrivalDate(vacationDto.getArrivalDate());
        customDate.setArrivalDayPart(Vacation.DayPart.getDayPartByNumber(vacationDto.getArrivalDayPart()));
        customDate.setLeaveDate(vacationDto.getLeaveDate());
        customDate.setLeaveDayPart(Vacation.DayPart.getDayPartByNumber(vacationDto.getLeaveDayPart()));

        vacation.setVacationDate(customDate);
        Set<RoomVacation> roomVacationSet = StreamSupport.stream(vacationRepository.findAll().spliterator(), false)
                .flatMap(e -> e.getRoomVacationSet().stream())
                .filter(el -> el.getVacation().getId().equals(vacationDto.getVacationId()))
                .collect(toSet());
        vacation.setRoomVacationSet(roomVacationSet);

        return vacation;
    }
}
