package com.demo.hotel_management.utils;

import com.demo.hotel_management.entity.Client;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClientCsvReaderWriter {

    private static final String CSV = ".csv";

    public static File writeClientsToFile(List<Client> allClients) throws IOException {
        File csvFile = new File("clients_" + LocalDate.now() + CSV);
        FileOutputStream fos = new FileOutputStream(csvFile);
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

        String header = "ID,NAME,OTHER_INFO,PHONE,COMMENT,INACTIVE";
        bw.write(header);
        bw.newLine();

        for (Client client : allClients) {
            String id = String.valueOf(client.getId());
            String name = client.getName();
            String otherInfo = client.getOtherClientInfo();
            String phone = client.getPhoneNumber();
            String comment = client.getComment();
            String inactive = String.valueOf(client.getInactive());

            String clientRecord = String.join(",", id, name, otherInfo, phone, comment, inactive);
            bw.write(clientRecord);
            bw.newLine();
        }
        bw.close();
        return csvFile;
    }

    public static List<Client> readClientsFromFile(InputStream in) throws IOException {
        List<Client> allClients = new ArrayList<>();

        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String header = br.readLine();
        String clientRecord;
        while ((clientRecord = br.readLine()) != null) {
            String[] clientData = clientRecord.split(",");
            Client client = new Client();
            client.setId(Long.valueOf(clientData[0]));
            client.setName(clientData[1]);
            client.setOtherClientInfo(clientData[2]);
            client.setPhoneNumber(clientData[3]);
            client.setComment(clientData[4]);
            client.setInactive(Boolean.valueOf(clientData[5]));

            allClients.add(client);
        }
        br.close();

        return allClients;
    }
}