package com.demo.hotel_management.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/*
* Intermediate table
*/
@Entity
@Data
public class RoomVacation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="bed_type")
    @CollectionTable(name="occupied_beds")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name="number_of_beds")
    private Map<Room.BedType, Integer> occupiedBeds = new HashMap<>();

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vac_id")
    private Vacation vacation;

}