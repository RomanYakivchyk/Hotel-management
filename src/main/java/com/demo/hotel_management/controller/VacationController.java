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

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Controller
public class VacationController {


    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int INITIAL_PAGE_SIZE = 5;
    private static final int[] PAGE_SIZES = {5, 10, 20};

    @Autowired
    private VacationService vacationService;

    @Autowired
    private ClientService clientService;

    @RequestMapping(value = {"/vacations/add", "/vacation/{vacationId}/edit"}, method = RequestMethod.GET)
    public String vacationEditForm(Model model, @PathVariable(required = false) Long vacationId) {
        log.debug("model={}, vacationId={}", vacationId);
        List<Client> allClients = clientService.findAll();
        if (null != vacationId) {
            model.addAttribute("vacationModel", vacationService.findById(vacationId));
        } else {
            model.addAttribute("vacationModel", new VacationDto());
        }
        model.addAttribute("clients", allClients);
        model.addAttribute("clientModel", new Client());

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
            return "vacationForm.html";
        }
        VacationDto savedVacation = vacationService.saveVacation(vacationDto);

        log.debug("model={}, savedVacation={}", model, savedVacation);
        return "redirect:/vacations";
    }

//    @RequestMapping(value = "/vacations", method = RequestMethod.GET)
//    public String listVacations(
//            Model model,
//            @RequestParam("page") Optional<Integer> page,
//            @RequestParam("size") Optional<Integer> size) {
//
//        log.debug("model={}, page={}, size={}", model.toString(), page.orElse(currentPage), size.orElse(pageSize));
//
//        page.ifPresent(p -> currentPage = p);
//        size.ifPresent(s -> pageSize = s);
//
//        Page<VacationDto> vacationDtoPage = vacationService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
//
//        model.addAttribute("vacationDtoPage", vacationDtoPage);
//
//        int totalPages = vacationDtoPage.getTotalPages();
//        if (totalPages > 0) {
//            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
//                    .boxed()
//                    .collect(Collectors.toList());
//            model.addAttribute("pageNumbers", pageNumbers);
//        }
//
//
//        log.debug("model={}, page={}, size={}", model.toString(), page.orElse(currentPage), size.orElse(pageSize));
//
//        return "listVacations.html";
//    }
@GetMapping("/vacations")
public ModelAndView listClients(@RequestParam("pageSize") Optional<Integer> pageSize,
                                @RequestParam("page") Optional<Integer> page) {
    ModelAndView modelAndView = new ModelAndView("listVacations.html");

    // Evaluate page size. If requested parameter is null, return initial
    // page size
    int evalPageSize = pageSize.orElse(INITIAL_PAGE_SIZE);
    // Evaluate page. If requested parameter is null or less than 0 (to
    // prevent exception), return initial size. Otherwise, return value of
    // param. decreased by 1.
    int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;

    Page<VacationDto> vacations = vacationService.findAllPageable(PageRequest.of(evalPage, evalPageSize));
    Pager pager = new Pager(vacations.getTotalPages(), vacations.getNumber(), BUTTONS_TO_SHOW);

    modelAndView.addObject("vacations", vacations);
    modelAndView.addObject("selectedPageSize", evalPageSize);
    modelAndView.addObject("pageSizes", PAGE_SIZES);
    modelAndView.addObject("pager", pager);
    return modelAndView;
}
}
