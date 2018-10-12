package com.demo.hotel_management.utils.custom_validators;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.Vacation;
import com.demo.hotel_management.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.util.Collections;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class VacationValidator implements ConstraintValidator<ValidateVacation, VacationDto> {

    @Autowired
    private VacationService vacationService;

    @Override //todo
    public boolean isValid(VacationDto vacationDto, ConstraintValidatorContext context) {

        boolean isValid = true;

        if (vacationDto.getArrivalDate() != null && vacationDto.getLeaveDate() != null) {

            Supplier<Stream<VacationDto>> vacationDtoOverlapSupplier =
                    () -> vacationService.getAllActiveVacations().stream()
                            .filter(e -> vacationDto.getArrivalDate().isBefore(e.getLeaveDate()))
                            .filter(e -> vacationDto.getLeaveDate().isAfter(e.getArrivalDate()))
                            .filter(e-> !Collections.disjoint(e.getRoomNumbers(), vacationDto.getRoomNumbers()));


            if (vacationDtoOverlapSupplier.get().findAny().isPresent()) {

                if (vacationDto.getHasSharedRooms()) {

//                    LocalDate startDate = vacationDto.getArrivalDate();
//                    LocalDate endDate = vacationDto.getLeaveDate();
//
//                    for (LocalDate l = startDate; startDate.isBefore(endDate.plusDays(1)); l = l.plusDays(1)) {
//                        LocalDate curr = l;
//                        long overlappedVacations = vacationDtoOverlapSupplier.get()
//                                .filter(e -> curr.isAfter(e.getArrivalDate().minusDays(1)))
//                                .filter(e -> curr.isBefore(e.getLeaveDate().plusDays(1)))
//                                .filter(e -> !(curr.equals(startDate) && curr.equals(e.getLeaveDate())))
//                                .filter(e -> !(curr.equals(endDate) && curr.equals(e.getArrivalDate())))
//                                .count();
//                        System.out.println("count= " + overlappedVacations);
//                        if (overlappedVacations > 1) return false;
//                    }

                } else {
                    isValid = vacationDtoOverlapSupplier.get()
                            .allMatch(e -> Collections.disjoint(e.getRoomNumbers(), vacationDto.getRoomNumbers()));
                }
            }
        }
        return isValid;
    }

}
