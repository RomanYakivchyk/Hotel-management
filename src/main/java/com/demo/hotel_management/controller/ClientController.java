package com.demo.hotel_management.controller;

import com.demo.hotel_management.dto.ClientDto;
import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.service.ClientService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@Slf4j
public class ClientController {

    private static int currentPage = 1;
    private static int pageSize = 5;

    @Autowired
    private ClientService clientService;

    @RequestMapping(value = "/clients", method = RequestMethod.GET)
    public String listClients(
            Model model,
            @RequestParam("page") Optional<Integer> page,
            @RequestParam("size") Optional<Integer> size) {

        log.debug("model={}, page={}, size={}", model.toString(), page.orElse(currentPage), size.orElse(pageSize));

        page.ifPresent(p -> currentPage = p);
        size.ifPresent(s -> pageSize = s);

        Page<ClientDto> clientPage = clientService.findPaginated(PageRequest.of(currentPage - 1, pageSize));

        model.addAttribute("clientPage", clientPage);

        int totalPages = clientPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }


        log.debug("model={}, page={}, size={}", model.toString(), page.orElse(currentPage), size.orElse(pageSize));

        return "listClients.html";
    }

    @RequestMapping(value = {"/clients/add", "/client/{clientId}/edit"}, method = RequestMethod.GET)
    public String clientEditForm(Model model, @PathVariable(required = false) Long clientId) {
        log.debug("model={}, clientId={}", clientId);

        if (null != clientId) {
            model.addAttribute("clientModel", clientService.findById(clientId));
        } else {
            model.addAttribute("clientModel", new ClientDto());
        }

        log.debug("model={}, clientId={}", clientId);
        return "clientForm.html";
    }

    @RequestMapping(path = "/clients/update", method = RequestMethod.POST)
    public String clientEdit(@Valid @ModelAttribute("clientModel") ClientDto clientDto, BindingResult result, Model model) {
        log.debug("clientDto={}, bindingErrors={}, bindingErrorsFields={}, model={}",
                clientDto, result.hasErrors(), result.getFieldErrors(), model);

        if (result.hasErrors()) {
            model.addAttribute("clientModel", clientDto);
            return "clientForm.html";
        }
        ClientDto savedClient = clientService.saveClient(clientDto);

        log.debug("model={}, savedClientDto={}", model, savedClient);
        return "redirect:/clients";
    }

    @RequestMapping(path = "/client/{clientId}/remove", method = RequestMethod.GET)
    public String removeClient(@PathVariable Long clientId) {
        log.debug("clientId={}", clientId);
        clientService.removeClient(clientId);
        log.debug("clientId={}", clientId);
        return "redirect:/clients";
    }
}
