package com.demo.hotel_management.entity.pagination.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;

@Entity
public class VacationModel {

    public VacationModel() {
    }

    @Id
    private String vacid;
    private String clientname;
    private String clientid;
    private LocalDate startdate;
    private LocalDate enddate;
    private String roomnumbers;
    private Integer totalrecords;

    public String getVacid() {
        return vacid;
    }

    public void setVacid(String vacid) {
        this.vacid = vacid;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public LocalDate getStartdate() {
        return startdate;
    }

    public void setStartdate(LocalDate startdate) {
        this.startdate = startdate;
    }

    public LocalDate getEnddate() {
        return enddate;
    }

    public void setEnddate(LocalDate enddate) {
        this.enddate = enddate;
    }

    public String getRoomnumbers() {
        return roomnumbers;
    }

    public void setRoomnumbers(String roomnumbers) {
        this.roomnumbers = roomnumbers;
    }

    public Integer getTotalrecords() {
        return totalrecords;
    }

    public void setTotalrecords(Integer totalrecords) {
        this.totalrecords = totalrecords;
    }
}
