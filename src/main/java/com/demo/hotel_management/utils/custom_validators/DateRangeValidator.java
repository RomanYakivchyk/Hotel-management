package com.demo.hotel_management.utils.custom_validators;

import com.demo.hotel_management.entity.Vacation;
import lombok.extern.slf4j.Slf4j;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class DateRangeValidator implements ConstraintValidator<ValidateDateRange, Vacation.CustomDate> {

    @Override
    public void initialize(ValidateDateRange constraintAnnotation) {

    }

    @Override
    public boolean isValid(Vacation.CustomDate customDate, ConstraintValidatorContext constraintValidatorContext) {
        log.debug("Custom date validation: arrivalDate={}, leaveDate={}", customDate.getArrivalDate(), customDate.getLeaveDate());
        return customDate.getLeaveDate().isAfter(customDate.getArrivalDate());
    }
}
