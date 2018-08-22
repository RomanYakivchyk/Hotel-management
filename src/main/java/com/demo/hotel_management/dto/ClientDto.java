package com.demo.hotel_management.dto;

import lombok.Data;

@Data
public class ClientDto {
    private Long id;
    private String name;
    private String otherClientInfo;
    private String phoneNumber;
    private String email;
}
