package com.demo.hotel_management.utils.custom_validators;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.*;
import javax.validation.Constraint;
import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {RoommateAllowanceValidator.class})
public @interface ValidateRoommateAllowance {
    String message() default "Виберіть кімнату для підселення";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}