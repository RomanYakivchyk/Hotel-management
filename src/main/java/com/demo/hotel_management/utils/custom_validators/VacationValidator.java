package com.demo.hotel_management.utils.custom_validators;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.RoomVacation;
import com.demo.hotel_management.entity.Vacation;
import com.demo.hotel_management.repository.VacationRepository;
import com.demo.hotel_management.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


public class VacationValidator implements ConstraintValidator<ValidateVacation, VacationDto> {

    @Autowired
    private VacationService vacationService;

    @Autowired
    private VacationRepository vacationRepository;

    @Override //todo  test this logic
    public boolean isValid(VacationDto vacationDto, ConstraintValidatorContext context) {

        boolean isValid = true;

        if (vacationDto.getArrivalDate() != null && vacationDto.getLeaveDate() != null) {

            List<Vacation> allActiveVacations = vacationRepository.findByInactiveFalse();

            List<RoomVacation> overlappedVacations = allActiveVacations.stream()
                    .filter(e -> dateRangesOverlap(vacationDto.getArrivalDate(), vacationDto.getLeaveDate(),
                            e.getVacationDate().getArrivalDate(), e.getVacationDate().getLeaveDate()))
                    .flatMap(e -> e.getRoomVacationList().stream())
                    .collect(toList());


            if (overlappedVacations.size() > 0) {

                for (Integer roomNumber : vacationDto.getRoomNumbers()) {

                    List<RoomVacation> allRoomVacations = overlappedVacations.stream()
                            .filter(e -> e.getRoom().getRoomNumber().equals(roomNumber))
                            .collect(toList());

                    List<RoomVacation> allowRoommateRoomVacations = allRoomVacations.stream()
                            .filter(RoomVacation::getAllowRoommate)
                            .collect(toList());

                    List<RoomVacation> notAllowRoommateRoomVacations = allRoomVacations.stream()
                            .filter(e -> !e.getAllowRoommate())
                            .collect(toList());


                    if (vacationDto.getSharedRoomNumbers().contains(roomNumber)) {

                        if (notAllowRoommateRoomVacations.size() > 0) isValid = false;

                        if (allowRoommateRoomVacations.size() > 1) {
                            if (roomVacationsOverlap(allowRoommateRoomVacations)) isValid = false;
                        }

                    } else {
                        if (allRoomVacations.size() > 0) isValid = false;
                    }

                }
            }
        }
        return isValid;
    }

    private boolean roomVacationsOverlap(List<RoomVacation> roomVacationList) {
        boolean overlap = false;
        for (int i = 1; i < roomVacationList.size(); i++) {
            LocalDate startDate1 = roomVacationList.get(i - 1).getVacation().getVacationDate().getArrivalDate();
            LocalDate endDate1 = roomVacationList.get(i - 1).getVacation().getVacationDate().getLeaveDate();

            LocalDate startDate2 = roomVacationList.get(i).getVacation().getVacationDate().getArrivalDate();
            LocalDate endDate2 = roomVacationList.get(i).getVacation().getVacationDate().getLeaveDate();

            if (dateRangesOverlap(startDate1, endDate1, startDate2, endDate2)) {
                overlap = true;
            }
        }
        return overlap;
    }

    private boolean dateRangesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

}
