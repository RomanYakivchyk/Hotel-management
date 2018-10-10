package com.demo.hotel_management.utils.custom_validators;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
        import java.lang.annotation.*;
        import javax.validation.Constraint;
        import javax.validation.Payload;

@Target(ElementType.TYPE)
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {VacationValidator.class})
public @interface ValidateVacation {
    String message() default "replace with error message";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}