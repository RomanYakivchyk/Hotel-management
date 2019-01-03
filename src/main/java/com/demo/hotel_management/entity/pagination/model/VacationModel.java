package com.demo.hotel_management.entity.pagination.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;

@Getter
@Setter
@Entity
public class VacationModel {
    @Id
    private String vacid;
    private String clientname;
    private String clientid;
    private LocalDate startdate;
    private LocalDate enddate;
    private String roomnumbers;
    private Integer totalrecords;
}
