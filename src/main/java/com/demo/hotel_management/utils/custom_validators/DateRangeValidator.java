package com.demo.hotel_management.utils.custom_validators;

import com.demo.hotel_management.dto.ClientDto;
import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.Vacation;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Slf4j
public class DateRangeValidator implements ConstraintValidator<ValidateDateRange, VacationDto> {

    @Override
    public void initialize(ValidateDateRange constraintAnnotation) {

    }

    @Override
    public boolean isValid(VacationDto vacationDto, ConstraintValidatorContext constraintValidatorContext) {

        LocalDate arrivalDate = vacationDto.getArrivalDate();
        LocalDate leaveDate = vacationDto.getLeaveDate();
        Vacation.DayPart arrivalDayPart = Vacation.DayPart.getDayPartByNumber(vacationDto.getArrivalDayPart());
        Vacation.DayPart leaveDayPart = Vacation.DayPart.getDayPartByNumber(vacationDto.getLeaveDayPart());

        if (arrivalDate.equals(leaveDate))
            return arrivalDayPart.getDayPartNumber() <= leaveDayPart.getDayPartNumber();
        return leaveDate.isAfter(arrivalDate);
    }
}
