package com.demo.hotel_management.controller;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.service.ClientService;
import com.demo.hotel_management.utils.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Slf4j
public class ClientController {

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = {5, 10, 20};

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients")
    public ModelAndView listClients(@RequestParam("pageSize") Optional<Integer> pageSize,
                                    @RequestParam("page") Optional<Integer> page) {
        ModelAndView modelAndView = new ModelAndView("listClients.html");

        // Evaluate page size. If requested parameter is null, return initial
        // page size
        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
        // Evaluate page. If requested parameter is null or less than 0 (to
        // prevent exception), return initial size. Otherwise, return value of
        // param. decreased by 1.
        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

        Page<Client> clients = clientService.findAllPageable(PageRequest.of(evalPage, evalPageSize));
        Pager pager = new Pager(clients.getTotalPages(), clients.getNumber(), BUTTONS_TO_SHOW);

        modelAndView.addObject("clients", clients);
        modelAndView.addObject("selectedPageSize", evalPageSize);
        modelAndView.addObject("pageSizes", PAGE_SIZES);
        modelAndView.addObject("pager", pager);
        return modelAndView;
    }

    @RequestMapping(value = {"/clients/add", "/client/{clientId}/edit"}, method = RequestMethod.GET)
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
        System.out.println("================================");
        System.out.println(client);
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
