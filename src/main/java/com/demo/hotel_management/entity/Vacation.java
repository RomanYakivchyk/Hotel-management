package com.demo.hotel_management.entity;

import com.demo.hotel_management.utils.custom_validators.ValidateDateRange;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @NotNull
    @Min(1)
    @Max(20)
    private Integer residentsCount;

    @NotNull
    @ValidateDateRange
    @Embedded
    private CustomDate vacationDate;
    private Boolean withChildren;
    private Boolean withAnimals;

    @OneToMany(mappedBy = "vacation")
    private Set<RoomVacation> roomVacationSet = new HashSet<>();

    @Embeddable
    @Access(AccessType.FIELD)
    @Data
    public static class CustomDate {
        @NotNull
        private LocalDate arrivalDate;

        @NotNull
        @Enumerated(EnumType.STRING)
        private Vacation.DayPart arrivalDayPart;

        @NotNull
        private LocalDate leaveDate;

        @NotNull
        @Enumerated(EnumType.STRING)
        private Vacation.DayPart leaveDayPart;
    }

    enum DayPart {
        MORNING,
        AFTERNOON,
        EVENING
    }
}
