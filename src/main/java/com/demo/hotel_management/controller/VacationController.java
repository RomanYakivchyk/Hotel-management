package com.demo.hotel_management.controller;

import com.demo.hotel_management.dto.ClientDto;
import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.repository.VacationRepository;
import com.demo.hotel_management.service.ClientService;
import com.demo.hotel_management.service.VacationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class VacationController {

    @Autowired
    private VacationService vacationService;

    @Autowired
    private ClientService clientService;

    @RequestMapping(value = {"/vacations/add", "/vacation/{vacationId}/edit"}, method = RequestMethod.GET)
    public String vacationEditForm(Model model, @PathVariable(required = false) Long vacationId) {
        log.debug("model={}, vacationId={}", vacationId);
        List<ClientDto> allClients = clientService.findAll();
        if (null != vacationId) {
            model.addAttribute("vacationModel", vacationService.findById(vacationId));
            model.addAttribute("clients", allClients);
        } else {
            model.addAttribute("vacationModel", new VacationDto());
            model.addAttribute("clients", allClients);
        }

        log.debug("model={}, vacationId={}, clientList={}", model, vacationId, allClients);
        return "vacationForm.html";
    }


    @RequestMapping(path = "/vacations/update", method = RequestMethod.POST)
    public String vacationEdit(@Valid @ModelAttribute("vacationModel") VacationDto vacationDto, BindingResult result, Model model) {
        log.debug("vacationDto={}, bindingErrors={}, bindingErrorsFields={}, model={}",
                vacationDto, result.hasErrors(), result.getFieldErrors(), model);

        if (result.hasErrors()) {
            model.addAttribute("vacationModel", vacationDto);
            model.addAttribute("clients", clientService.findAll());
            return "clientForm.html";
        }
        VacationDto savedVacation = vacationService.saveVacation(vacationDto);

        log.debug("model={}, savedVacation={}", model, savedVacation);
        return "redirect:/vacations";
    }
}
