package com.demo.hotel_management.controller;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.entity.Vacation;
import com.demo.hotel_management.service.ClientService;
import com.demo.hotel_management.service.VacationService;
import com.demo.hotel_management.utils.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
public class VacationController {


    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 10;
    private static final int[] PAGE_SIZES = {5, 10, 20};

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

//    @GetMapping("/vacations")
//    public ModelAndView listClients(@RequestParam("pageSize") Optional<Integer> pageSize,
//                                    @RequestParam("page") Optional<Integer> page) {
//        ModelAndView modelAndView = new ModelAndView("listVacations.html");
//
//        // Evaluate page size. If requested parameter is null, return initial
//        // page size
//        int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
//        // Evaluate page. If requested parameter is null or less than 0 (to
//        // prevent exception), return initial size. Otherwise, return value of
//        // param. decreased by 1.
//        int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
//
//        Page<VacationDto> vacations = vacationService.findAllPageable(PageRequest.of(evalPage, evalPageSize));
//        Pager pager = new Pager(vacations.getTotalPages(), vacations.getNumber(), BUTTONS_TO_SHOW);
//
//        modelAndView.addObject("vacations", vacations);
//        modelAndView.addObject("selectedPageSize", evalPageSize);
//        modelAndView.addObject("pageSizes", PAGE_SIZES);
//        modelAndView.addObject("pager", pager);
//        return modelAndView;
//    }




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
