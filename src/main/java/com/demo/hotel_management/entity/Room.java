package com.demo.hotel_management.entity;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Data
public class Room {
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
    private Map<BedType, Integer> beds = new HashMap<>();

    @OneToMany(mappedBy = "room")
    private Set<RoomVacation> roomVacationSet = new HashSet<>();

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "building_id")
    private Building building;

    enum BedType {
        SINGLE_BED,
        DOUBLE_BED,
        FOLDING_BED
    }
}
