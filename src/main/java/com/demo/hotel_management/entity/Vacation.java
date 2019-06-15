package com.demo.hotel_management.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Entity
public class Vacation {

    public Vacation() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Boolean inactive;

    private Boolean approved;

    @OneToOne
    @JoinColumn(name = "client_id")
    private Client client;

    private Integer residentsCount;

    private Integer pricePerDay;
    private Integer totalPrice;

    @Embedded
    private CustomDate vacationDate;
//    private Boolean withChildren;

    @OneToMany(mappedBy = "vacation", cascade = {CascadeType.ALL})
    private List<RoomVacation> roomVacationList = new ArrayList<>();

    @Embeddable
    @Access(AccessType.FIELD)
    public static class CustomDate {

        public CustomDate() {
        }

        private LocalDate arrivalDate;

        @Enumerated(EnumType.STRING)
        private DayPart arrivalDayPart;

        private LocalDate leaveDate;

        @Enumerated(EnumType.STRING)
        private DayPart leaveDayPart;

        public LocalDate getArrivalDate() {
            return arrivalDate;
        }

        public void setArrivalDate(LocalDate arrivalDate) {
            this.arrivalDate = arrivalDate;
        }

        public DayPart getArrivalDayPart() {
            return arrivalDayPart;
        }

        public void setArrivalDayPart(DayPart arrivalDayPart) {
            this.arrivalDayPart = arrivalDayPart;
        }

        public LocalDate getLeaveDate() {
            return leaveDate;
        }

        public void setLeaveDate(LocalDate leaveDate) {
            this.leaveDate = leaveDate;
        }

        public DayPart getLeaveDayPart() {
            return leaveDayPart;
        }

        public void setLeaveDayPart(DayPart leaveDayPart) {
            this.leaveDayPart = leaveDayPart;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CustomDate that = (CustomDate) o;
            return Objects.equals(arrivalDate, that.arrivalDate) &&
                    arrivalDayPart == that.arrivalDayPart &&
                    Objects.equals(leaveDate, that.leaveDate) &&
                    leaveDayPart == that.leaveDayPart;
        }

        @Override
        public int hashCode() {
            return Objects.hash(arrivalDate, arrivalDayPart, leaveDate, leaveDayPart);
        }
    }


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

        public Integer getDayPartNumber() {
            return dayPartNumber;
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }

    public Boolean getApproved() {
        return approved;
    }

    public void setApproved(Boolean approved) {
        this.approved = approved;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getResidentsCount() {
        return residentsCount;
    }

    public void setResidentsCount(Integer residentsCount) {
        this.residentsCount = residentsCount;
    }

    public Integer getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Integer pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public CustomDate getVacationDate() {
        return vacationDate;
    }

    public void setVacationDate(CustomDate vacationDate) {
        this.vacationDate = vacationDate;
    }

    public List<RoomVacation> getRoomVacationList() {
        return roomVacationList;
    }

    public void setRoomVacationList(List<RoomVacation> roomVacationList) {
        this.roomVacationList = roomVacationList;
    }
}
