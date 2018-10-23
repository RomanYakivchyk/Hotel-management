package com.demo.hotel_management.service;

import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.exceptions.ClientNotFoundException;
import com.demo.hotel_management.repository.ClientRepository;
import com.demo.hotel_management.utils.EntityDtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    public Client findById(Long clientId) {
        log.debug("clientId={}", clientId);
        Client foundClient = clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
        log.debug("clientId={}, foundClient={}", clientId, foundClient);
        return foundClient;
    }


    public Page<Client> findAllPageable(Pageable pageable) {
        List<Client> clients = clientRepository.findByInactiveFalse();

        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > clients.size() ? clients.size() : (start + pageable.getPageSize());

        return new PageImpl<>(clients.subList(start, end), pageable, clients.size());
    }

    public Client saveClient(Client client) {

        log.debug("client={}", client);
        Client savedClient = clientRepository.save(client);
        log.debug("savedClient={}", savedClient);

        return savedClient;
    }

    public void inactivateClient(Long clientId) {

        log.debug("clientId={}", clientId);

        Client client = clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
        clientRepository.inactivate(client.getId());
        log.debug("Client inactivated: client={}", client);

    }

    public List<Client> findAllActive() {
        return clientRepository.findByInactiveFalse();
    }
}
