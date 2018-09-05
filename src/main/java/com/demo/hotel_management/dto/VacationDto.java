package com.demo.hotel_management.dto;

import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.entity.Vacation;
import com.demo.hotel_management.utils.custom_validators.ValidateDateRange;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
//@ValidateDateRange
public class VacationDto {

    private Long vacationId;
    @NotNull
    private Long clientId;
    private String clientName;
    private String clientOtherInfo;
    private String clientPhoneNumber;
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate arrivalDate;
    @NotNull
    private Integer arrivalDayPart;
    @NotNull
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate leaveDate;
    @NotNull
    private Integer leaveDayPart;
    @NotNull
    private Set<Long> roomIds;
    @Min(1)
    @Max(20)
    @NotNull
    private Integer residentsCount;
}
