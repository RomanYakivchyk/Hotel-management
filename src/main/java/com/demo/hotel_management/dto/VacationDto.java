package com.demo.hotel_management.dto;

import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.entity.Vacation;
import com.demo.hotel_management.utils.custom_validators.ValidateDateRange;
import lombok.Data;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Date;
import java.util.Set;

@Data
@ToString
//@ValidateDateRange
public class VacationDto {

    private Long vacationId;
    @NotNull(message = "{validation.vacation.client.message}")
    private Long clientId;
    private String clientName;
    private String clientOtherInfo;
    private String clientPhoneNumber;
    @NotNull(message = "{validation.vacation.arrival_date.message}")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate arrivalDate;
    @NotNull
    private Integer arrivalDayPart;
    @NotNull(message = "{validation.vacation.leave_date.message}")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate leaveDate;
    @NotNull
    private Integer leaveDayPart;
    @NotNull
    @NotEmpty(message = "{validation.vacation.rooms.message}")
    private Set<Integer> roomNumbers;
    @Min(1)
    @Max(20)
    @NotNull(message = "{validation.vacation.residents.message}")
    private Integer residentsCount;
}
