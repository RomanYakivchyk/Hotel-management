package com.demo.hotel_management.entity.pagination.model;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ClientModel {

    public ClientModel() {
    }

    @Id
    private Long id;
    private String name;
    private String otherclientinfo;
    private String phonenumber;
    private Integer totalrecords;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOtherclientinfo() {
        return otherclientinfo;
    }

    public void setOtherclientinfo(String otherclientinfo) {
        this.otherclientinfo = otherclientinfo;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public Integer getTotalrecords() {
        return totalrecords;
    }

    public void setTotalrecords(Integer totalrecords) {
        this.totalrecords = totalrecords;
    }
}
