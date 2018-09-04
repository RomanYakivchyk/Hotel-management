package com.demo.hotel_management.utils.custom_validators;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {DateRangeValidator.class})
public @interface ValidateDateRange {
    String message() default "The Leave Date must be after the Arrival Date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}