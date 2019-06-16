package com.demo.hotel_management.utils;

import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
public class ClientDataImporter {

    @Autowired
    private ClientRepository clientRepository;

    public void process(byte[] clientData) throws IOException {

        InputStream in = new ByteArrayInputStream(clientData);
            List<Client> clients = ClientCsvReaderWriter.readClientsFromFile(in);
            clients.forEach(client -> {
                boolean exist = clientRepository.existsById(client.getId());
                if (exist) {
                    clientRepository.findById(client.getId()).ifPresent(c -> {
                        c.setName(client.getName());
                        c.setOtherClientInfo(client.getOtherClientInfo());
                        c.setPhoneNumber(client.getPhoneNumber());
                        c.setComment(client.getComment());
                        c.setInactive(client.getInactive());
                        clientRepository.save(c);
                    });
                } else {
                    client.setId(null);
                    clientRepository.save(client);
                }
            });
    }
}
