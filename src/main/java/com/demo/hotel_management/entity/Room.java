package com.demo.hotel_management.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
public class Room {

    public Room() {
    }

    public Room(@Min(1) @Max(20) Integer roomNumber, List<RoomVacation> roomVacationList, @NotNull Building building) {
        this.roomNumber = roomNumber;
        this.roomVacationList = roomVacationList;
        this.building = building;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(1)
    @Max(20)
    private Integer roomNumber;
    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL)
    private List<RoomVacation> roomVacationList;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public List<RoomVacation> getRoomVacationList() {
        return roomVacationList;
    }

    public void setRoomVacationList(List<RoomVacation> roomVacationList) {
        this.roomVacationList = roomVacationList;
    }

    public Building getBuilding() {
        return building;
    }

    public void setBuilding(Building building) {
        this.building = building;
    }
}
