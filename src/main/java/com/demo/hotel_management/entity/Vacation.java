package com.demo.hotel_management.entity;

import com.demo.hotel_management.utils.custom_validators.ValidateDateRange;
import lombok.Data;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Entity
@Data
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    private Integer residentsCount;

    @Embedded
    private CustomDate vacationDate;
    private Boolean withChildren;

    @OneToMany(mappedBy = "vacation")
    private Set<RoomVacation> roomVacationSet = new HashSet<>();

    @Embeddable
    @Access(AccessType.FIELD)
    @Data
    public static class CustomDate {

        private LocalDate arrivalDate;

        @Enumerated(EnumType.STRING)
        private Vacation.DayPart arrivalDayPart;

        private LocalDate leaveDate;

        @Enumerated(EnumType.STRING)
        private Vacation.DayPart leaveDayPart;

    }

    @Getter
    public enum DayPart {

        MORNING(1),
        AFTERNOON(2),
        EVENING(3);

        DayPart(Integer dayPartNumber) {
            this.dayPartNumber = dayPartNumber;
        }

        Integer dayPartNumber;

        public static DayPart getDayPartByNumber(Integer dayPartNumber) {
            if (dayPartNumber == 1) return MORNING;
            else if (dayPartNumber == 2) return AFTERNOON;
            else if (dayPartNumber == 3) return EVENING;
            throw new NoSuchElementException();
        }
    }
}
