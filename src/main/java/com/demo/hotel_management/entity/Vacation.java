package com.demo.hotel_management.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Entity
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Vacation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean inactive;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private Integer residentsCount;

    @Embedded
    private CustomDate vacationDate;
//    private Boolean withChildren;

    @OneToMany(mappedBy = "vacation", cascade = {CascadeType.ALL})
    private List<RoomVacation> roomVacationList = new ArrayList<>();

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
