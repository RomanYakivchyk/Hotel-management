package com.demo.hotel_management.dto;

import lombok.Data;
import org.springframework.context.annotation.PropertySource;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ClientDto {

    private Long id;
    @NotNull
    @NotEmpty
    private String name;
    @NotNull
    @NotEmpty
    private String otherClientInfo;
    @NotNull
    @NotEmpty
    @Pattern(regexp = "^0[1-9]{9}$",message = "validation.client.phone.message")
    private String phoneNumber;
    @Email(message = "validation.client.email.message")
    private String email;
}
