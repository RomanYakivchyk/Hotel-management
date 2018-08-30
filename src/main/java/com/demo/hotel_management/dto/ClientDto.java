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
}
