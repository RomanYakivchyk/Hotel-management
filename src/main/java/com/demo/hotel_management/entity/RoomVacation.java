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
@Getter
@Setter
@NoArgsConstructor
public class RoomVacation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

//    @ElementCollection(fetch = FetchType.EAGER)
//    @MapKeyColumn(name="bed_type")
//    @CollectionTable(name="occupied_beds")
//    @MapKeyEnumerated(EnumType.STRING)
//    @Column(name="number_of_beds")
//    private Map<Room.BedType, Integer> occupiedBeds = new HashMap<>();

    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn(name = "vac_id")
    private Vacation vacation;

    private Boolean allowRoommate;

}