package com.demo.hotel_management.controller;

import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@Slf4j
public class ClientController {

    @Autowired
    private ClientService clientService;

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
}
