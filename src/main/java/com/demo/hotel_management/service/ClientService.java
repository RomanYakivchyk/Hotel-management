package com.demo.hotel_management.service;

import com.demo.hotel_management.dto.ClientDto;
import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.exceptions.ClientNotFoundException;
import com.demo.hotel_management.repository.ClientRepository;
import com.demo.hotel_management.utils.EntityDtoConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
//@Transactional
public class ClientService {

    @Autowired
    private EntityDtoConverter entityDtoConverter;

    @Autowired
    private ClientRepository clientRepository;

    public ClientDto findById(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
        return entityDtoConverter.convertClientEntityToDto(client);
    }

    public Page<ClientDto> findPaginated(Pageable pageable) {

        List<ClientDto> clientDtoList = StreamSupport.stream(clientRepository.findAll().spliterator(), false)
                .map(entityDtoConverter::convertClientEntityToDto)
                .collect(Collectors.toCollection(ArrayList::new));

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<ClientDto> list;

        if (clientDtoList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, clientDtoList.size());
            list = clientDtoList.subList(startItem, toIndex);
        }

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), clientDtoList.size());
    }

    public ClientDto saveClient(ClientDto clientDto) {
        Client client = entityDtoConverter.convertClientDtoToEntity(clientDto);
        Client savedClient = clientRepository.save(client);
        return entityDtoConverter.convertClientEntityToDto(savedClient);
    }

    public void removeClient(Long clientId) {
        Client client = clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
        clientRepository.delete(client);
    }
}
