package com.demo.hotel_management.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "{validation.client.name.message}")
    @NotEmpty(message = "{validation.client.name.message}")
    private String name;
    @NotNull(message = "{validation.client.other_info.message}")
    @NotEmpty(message = "{validation.client.other_info.message}")
    private String otherClientInfo;
    @Pattern(regexp = "^0[1-9]{9}$", message = "{validation.client.phone.message}")
    private String phoneNumber;
    @Email(message = "{validation.client.email.message}")
    private String email;
    private Boolean inactive = false;

}
