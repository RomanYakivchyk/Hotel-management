package com.demo.hotel_management.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
public class Client {

    public Client() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank(message = "{validation.client.name.message}")
    private String name;
    @NotBlank(message = "{validation.client.other_info.message}")
    private String otherClientInfo;
    //    @Pattern(regexp = "^0[1-9]{9}$", message = "{validation.client.phone.message}")
    @NotBlank(message = "{validation.client.phone.message}")
    private String phoneNumber;
    private String comment;
    private Boolean inactive = false;

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

    public String getOtherClientInfo() {
        return otherClientInfo;
    }

    public void setOtherClientInfo(String otherClientInfo) {
        this.otherClientInfo = otherClientInfo;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getInactive() {
        return inactive;
    }

    public void setInactive(Boolean inactive) {
        this.inactive = inactive;
    }



    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", otherClientInfo='" + otherClientInfo + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", comment='" + comment + '\'' +
                ", inactive=" + inactive +
                '}';
    }
}
