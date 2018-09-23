package com.demo.hotel_management.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Building {


    public Building(@Min(1) @Max(4) Integer buildingNumber, String name, List<Room> roomList) {
        this.buildingNumber = buildingNumber;
        this.name = name;
        this.roomList = roomList;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Min(1)
    @Max(4)
    private Integer buildingNumber;
    private String name;

    @OneToMany(mappedBy = "building",cascade = CascadeType.ALL)
    private List<Room> roomList;

}
