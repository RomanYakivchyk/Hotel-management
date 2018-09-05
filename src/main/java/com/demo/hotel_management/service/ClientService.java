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

    public List<Client> findAll() {
        //todo add sorting
        return StreamSupport.stream(clientRepository.findAll().spliterator(), false)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public Page<Client> findPaginated(Pageable pageable) {
        log.debug("pageable={}", pageable);


        Collator collator = Collator.getInstance(Locale.forLanguageTag("uk-UA"));

        List<Client> clientList = StreamSupport.stream(clientRepository.findAll().spliterator(), false)
                .sorted((o1, o2) -> {
                    int result = 0;
                    result = collator.compare(o1.getName(), o2.getName());
                    if (result == 0) {
                        result = collator.compare(o1.getOtherClientInfo(), o2.getOtherClientInfo());
                    }
                    return result;
                })
                .collect(Collectors.toList());

        log.debug("pageable={}", clientList);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Client> list;

        if (clientList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, clientList.size());
            list = clientList.subList(startItem, toIndex);
        }

        log.debug("pageSize={}, currentPage={}, startItem={}, List<Client>={} ", pageSize, currentPage, startItem, list);

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), clientList.size());
    }

    public Client saveClient(Client client) {

        log.debug("client={}", client);
        Client savedClient = clientRepository.save(client);
        log.debug("savedClient={}", savedClient);

        return savedClient;
    }

    public void removeClient(Long clientId) {

        log.debug("clientId={}", clientId);

        Client client = clientRepository.findById(clientId).orElseThrow(ClientNotFoundException::new);
        clientRepository.delete(client);
        log.debug("Client removed: client={}", client);

    }
}
