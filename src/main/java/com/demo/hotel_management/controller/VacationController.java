package com.demo.hotel_management.controller;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.service.ClientService;
import com.demo.hotel_management.service.VacationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Slf4j
@Controller
public class VacationController {

    @Autowired
    private VacationService vacationService;

    @Autowired
    private ClientService clientService;


    @GetMapping("/")
    public String showTable() {
        return "index.html";
    }

    @RequestMapping(value = {"/vacations/update", "/vacation/{vacationId}/edit"}, method = RequestMethod.GET)
    public String vacationEditForm(Model model, @PathVariable(required = false) Long vacationId, HttpServletRequest request) {
        log.debug("model={}, vacationId={}", vacationId);

        List<Client> allActiveClients = clientService.findAllActive();
        if (null != vacationId) {
            model.addAttribute("vacationModel", vacationService.findById(vacationId));
        } else {
            model.addAttribute("vacationModel", new VacationDto());
        }
        model.addAttribute("clients", allActiveClients);
        model.addAttribute("clientModel", new Client());


        log.debug("model={}, vacationId={}, clientList={}", model, vacationId, allActiveClients);
        return "vacationForm.html";
    }


    @RequestMapping(path = "/vacations/update", method = RequestMethod.POST)
    public String vacationEdit(@Valid @ModelAttribute("vacationModel") VacationDto vacationDto, BindingResult result, Model model) {
        log.debug("vacationDto={}, bindingErrors={}, bindingErrorsFields={}, model={}",
                vacationDto, result.hasErrors(), result.getFieldErrors(), model);

        if (result.hasErrors()) {
            model.addAttribute("vacationModel", vacationDto);

            model.addAttribute("clients", clientService.findAllActive());
            return "vacationForm.html";
        }
        VacationDto savedVacation = vacationService.saveVacation(vacationDto);

        log.debug("model={}, savedVacation={}", model, savedVacation);
        return "redirect:/vacations";
    }

    @GetMapping("/vacations")
    public String listClients(Model model){
        model.addAttribute("vacations",vacationService.getAllActiveVacations());
        return "listVacations.html";
    }

    @GetMapping("/vacation/{id}/approve")
    @ResponseBody
    public String approveVacation(@PathVariable(name = "id") Long id,
                                  @RequestParam(name = "approval") Boolean approval){
        vacationService.approveVacation(id,approval);
        return "";
    }


    @RequestMapping(path = "/vacation/{vacId}/remove", method = RequestMethod.GET)
    public String removeVacation(@PathVariable Long vacId) {
        log.debug("vacId={}", vacId);
        vacationService.inactivateVacation(vacId);
        log.debug("vacId={}", vacId);
        return "redirect:/vacations";
    }

    @GetMapping(path = "/vacation/{vacId}/remove-ajax")
    @ResponseBody
    public String removeVacationAjax(@PathVariable Long vacId) {
        vacationService.inactivateVacation(vacId);
        return "";
    }
}
