package com.demo.hotel_management.utils;

import com.demo.hotel_management.dto.ClientDto;
import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.entity.Vacation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EntityDtoConverter {
    @Autowired
    private ModelMapper modelMapper;

    public ClientDto convertClientEntityToDto(Client client) {
        return modelMapper.map(client, ClientDto.class);
    }

    public Client convertClientDtoToEntity(ClientDto clientDto) {
        return modelMapper.map(clientDto, Client.class);
    }

}
