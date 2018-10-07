package com.demo.hotel_management.utils.custom_validators;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.Vacation;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Slf4j
public class RoommateAllowanceValidator implements ConstraintValidator<ValidateRoommateAllowance, VacationDto> {

    @Override
    public boolean isValid(VacationDto vacationDto, ConstraintValidatorContext context) {

        boolean allowRoommate = vacationDto.isAllowRoommate();
        Set<Integer> sharedRoomNumbers = vacationDto.getSharedRoomNumbers();
        boolean isValid = true;
        if (allowRoommate) {
            isValid = (sharedRoomNumbers != null && !sharedRoomNumbers.isEmpty());
        }

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate()).addNode("sharedRoomNumbers").addConstraintViolation();
        }

        return isValid;
    }
}
