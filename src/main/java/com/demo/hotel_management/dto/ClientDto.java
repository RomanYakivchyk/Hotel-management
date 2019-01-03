package com.demo.hotel_management.dto;

import jdk.nashorn.internal.objects.annotations.Constructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class ClientDto {

    public ClientDto() {
    }

    @Id
    private Long id;
    private String name;
    private String otherclientinfo;
    private String phonenumber;
    private String email;
    private Integer totalrecords;
}
