package com.demo.hotel_management.controller;

import com.demo.hotel_management.entity.pagination.model.ClientModel;
import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.entity.pagination.DataTableRequest;
import com.demo.hotel_management.entity.pagination.DataTableResults;
import com.demo.hotel_management.entity.pagination.PaginationCriteria;
import com.demo.hotel_management.service.ClientService;
import com.demo.hotel_management.utils.AppUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

@Controller
public class ClientController {

    private static Logger log = LoggerFactory.getLogger(ClientController.class);

    @Autowired
    private ClientService clientService;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/clients")
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.findAllActive());
        return "listClients.html";
    }


    @RequestMapping(value = {"/clients/update", "/client/{clientId}/edit"}, method = RequestMethod.GET)
    public String clientEditForm(Model model, @PathVariable(required = false) Long clientId) {
        log.debug("model={}, clientId={}", clientId);

        if (null != clientId) {
            model.addAttribute("clientModel", clientService.findById(clientId));
        } else {
            model.addAttribute("clientModel", new Client());
        }

        log.debug("model={}, clientId={}", model, clientId);
        return "clientForm.html";
    }

    @RequestMapping(path = "/clients/update", method = RequestMethod.POST)
    public String clientEdit(@Valid @ModelAttribute("clientModel") Client client, BindingResult result, Model model) {
        log.debug("client={}, bindingErrors={}, bindingErrorsFields={}, model={}",
                client, result.hasErrors(), result.getFieldErrors(), model);

        if (result.hasErrors()) {
            model.addAttribute("clientModel", client);
            return "clientForm.html";
        }
        Client savedClient = clientService.saveClient(client);

        log.debug("model={}, savedClient={}", model, savedClient);
        return "redirect:/clients";
    }


    @RequestMapping(path = "/clients/create-ajax", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public String[] clientEditAjax(Client client) {

        Client createdClient = clientService.saveClient(client);
        String clientInfo = createdClient.getName() + " " + createdClient.getOtherClientInfo() + " " + createdClient.getPhoneNumber();
        String clientId = String.valueOf(createdClient.getId());

        String[] arr = new String[2];
        arr[0] = clientId;
        arr[1] = clientInfo;
        return arr;
    }

    @RequestMapping(path = "/client/{clientId}/remove", method = RequestMethod.GET)
    public String removeClient(@PathVariable Long clientId) {
        log.debug("clientId={}", clientId);
        clientService.inactivateClient(clientId);
        log.debug("clientId={}", clientId);
        return "redirect:/clients";
    }


    @RequestMapping(value = "/clients/paginated", method = RequestMethod.GET)
    @ResponseBody
    public String listClientsPaginated(HttpServletRequest request, HttpServletResponse response, Model model) {

        DataTableRequest<Client> dataTableInRQ = new DataTableRequest<>(request);
        PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();

        String baseQuery = "SELECT id, name, other_client_info as otherclientinfo, phone_number as phonenumber,(SELECT COUNT(1) FROM client) AS totalrecords FROM client";
        String paginatedQuery = AppUtil.buildPaginatedQuery(baseQuery, pagination);

        System.out.println(paginatedQuery);

        Query query = entityManager.createNativeQuery(paginatedQuery, ClientModel.class);

        @SuppressWarnings("unchecked")
        List<ClientModel> clientList = query.getResultList();

        DataTableResults<ClientModel> dataTableResult = new DataTableResults<>();
        dataTableResult.setDraw(dataTableInRQ.getDraw());
        dataTableResult.setListOfDataObjects(clientList);
        if (!AppUtil.isObjectEmpty(clientList)) {
            dataTableResult.setRecordsTotal(clientList.get(0).getTotalrecords()
                    .toString());
            if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
                dataTableResult.setRecordsFiltered(clientList.get(0).getTotalrecords()
                        .toString());
            } else {
                dataTableResult.setRecordsFiltered(Integer.toString(clientList.size()));
            }
        }
        return new Gson().toJson(dataTableResult);
    }


}
