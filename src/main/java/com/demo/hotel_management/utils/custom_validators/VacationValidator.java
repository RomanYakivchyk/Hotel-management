package com.demo.hotel_management.utils.custom_validators;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.repository.VacationRepository;
import com.demo.hotel_management.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;


public class VacationValidator implements ConstraintValidator<ValidateVacation, VacationDto> {

    @Autowired
    private VacationService vacationService;

    @Override
    public boolean isValid(VacationDto vacationDto, ConstraintValidatorContext context) {

        boolean isValid = true;

        if (vacationDto.getArrivalDate() != null && vacationDto.getLeaveDate() != null) {

            if (vacationDto.getHasSharedRooms()) {
                //todo
            } else {
                isValid = vacationService.getAllActiveVacations().stream()
                        .filter(e -> (vacationDto.getArrivalDate().isBefore(e.getLeaveDate())
                                && vacationDto.getLeaveDate().isAfter(e.getArrivalDate())
                        ))
                        .allMatch(e -> Collections.disjoint(e.getRoomNumbers(), vacationDto.getRoomNumbers()));
            }

        }
        return isValid;
    }

    private boolean isBeforeOrEqual(LocalDate localDate1, LocalDate localDate2) {
        return localDate1.isBefore(localDate2) || localDate1.isEqual(localDate2);
    }

    private boolean isAfterOrEqual(LocalDate localDate1, LocalDate localDate2) {
        return localDate1.isAfter(localDate2) || localDate1.isEqual(localDate2);
    }
}
