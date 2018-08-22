package com.demo.hotel_management.utils.custom_validators;

import com.demo.hotel_management.entity.Vacation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class DateRangeValidator implements ConstraintValidator<ValidateDateRange, Vacation.CustomDate> {

    @Override
    public void initialize(ValidateDateRange constraintAnnotation) {

    }

    @Override
    public boolean isValid(Vacation.CustomDate customDate, ConstraintValidatorContext constraintValidatorContext) {
        return customDate.getLeaveDate().isAfter(customDate.getArrivalDate());
    }
}
