package com.demo.hotel_management.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/*
* Intermediate table
*/
@Entity
public class RoomVacation {

    public RoomVacation() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "vac_id")
    private Vacation vacation;

    private Boolean allowRoommate = false;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Vacation getVacation() {
        return vacation;
    }

    public void setVacation(Vacation vacation) {
        this.vacation = vacation;
    }

    public Boolean getAllowRoommate() {
        return allowRoommate;
    }

    public void setAllowRoommate(Boolean allowRoommate) {
        this.allowRoommate = allowRoommate;
    }
}