package com.demo.hotel_management.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotNull
    @NotEmpty
    private String name;
    private String otherClientInfo;

    @Pattern(regexp = "^((\\+)?38)?\\d{10}$")
    private String phoneNumber;
    @Email
    private String email;

    //todo add a feedback field

}
