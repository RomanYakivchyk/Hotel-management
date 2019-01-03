package com.demo.hotel_management.controller;

import com.demo.hotel_management.dto.ClientDto;
import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.entity.pagination.DataTableRequest;
import com.demo.hotel_management.entity.pagination.DataTableResults;
import com.demo.hotel_management.entity.pagination.PaginationCriteria;
import com.demo.hotel_management.service.ClientService;
import com.demo.hotel_management.utils.AppUtil;
import com.demo.hotel_management.utils.EntityDtoConverter;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Controller
@Slf4j
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/clients")
    public String listClients(Model model) {
        model.addAttribute("clients", clientService.findAllActive());
        return "listClients.html";
    }

    @GetMapping("/clients/serverProcessing")
    @ResponseBody
    public List<Client> listClients() {
        return clientService.findAllActive();
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


    @RequestMapping(value = "/users/paginated", method = RequestMethod.GET)
    @ResponseBody
    public String listClientsPaginated(HttpServletRequest request, HttpServletResponse response, Model model) {

        DataTableRequest<Client> dataTableInRQ = new DataTableRequest<>(request);
        PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();

        String baseQuery = "SELECT id, name, other_client_info as otherclientinfo, phone_number as phonenumber, email as email,(SELECT COUNT(1) FROM client) AS totalrecords FROM client";
        String paginatedQuery = AppUtil.buildPaginatedQuery(baseQuery, pagination);

        System.out.println(paginatedQuery);

        Query query = entityManager.createNativeQuery(paginatedQuery, ClientDto.class);

        @SuppressWarnings("unchecked")
        List<ClientDto> clientList = query.getResultList();

        DataTableResults<ClientDto> dataTableResult = new DataTableResults<>();
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
