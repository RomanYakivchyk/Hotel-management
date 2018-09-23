package com.demo.hotel_management.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Room {

    public Room(@Min(1) @Max(20) Integer roomNumber, Map<BedType, Integer> beds, List<RoomVacation> roomVacationList, @NotNull Building building) {
        this.roomNumber = roomNumber;
        this.beds = beds;
        this.roomVacationList = roomVacationList;
        this.building = building;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(1)
    @Max(20)
    private Integer roomNumber;

    @ElementCollection(fetch = FetchType.EAGER)
    @MapKeyColumn(name="bed_type")
    @CollectionTable(name="beds")
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name="number_of_beds")
    private Map<BedType, Integer> beds;

    @OneToMany(mappedBy = "room",cascade = CascadeType.ALL)
    private List<RoomVacation> roomVacationList;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;

    public enum BedType {
        SINGLE_BED,
        DOUBLE_BED,
        FOLDING_BED
    }
}
