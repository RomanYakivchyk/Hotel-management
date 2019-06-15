package com.demo.hotel_management.controller;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.entity.Vacation;
import com.demo.hotel_management.entity.pagination.DataTableRequest;
import com.demo.hotel_management.entity.pagination.DataTableResults;
import com.demo.hotel_management.entity.pagination.PaginationCriteria;
import com.demo.hotel_management.entity.pagination.model.VacationModel;
import com.demo.hotel_management.service.ClientService;
import com.demo.hotel_management.service.VacationService;
import com.demo.hotel_management.utils.AppUtil;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;


@Controller
public class VacationController {

    private static Logger log = LoggerFactory.getLogger(VacationController.class);


    @Autowired
    private VacationService vacationService;

    @Autowired
    private ClientService clientService;

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/")
    public String showTable() {
        return "index.html";
    }

    @RequestMapping(value = {"/login"})
    public String login() {
        return "login.html";
    }

    @RequestMapping(value = {"/vacations/update", "/vacation/{vacationId}/edit"}, method = RequestMethod.GET)
    public String vacationEditForm(Model model, HttpServletRequest request,
                                   @PathVariable(required = false) Long vacationId,
                                   @DateTimeFormat(pattern = "dd/MM/yyyy")
                                   @RequestParam(value = "startDate", required = false) LocalDate startDate,
                                   @DateTimeFormat(pattern = "dd/MM/yyyy")
                                   @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                   @RequestParam(value = "rooms", required = false) Set<Integer> rooms,
                                   @RequestParam(value = "sharedRooms", required = false) Set<Integer> sharedRooms
    ) {
        log.debug("model={}, vacationId={}", vacationId);

        List<Client> allActiveClients = clientService.findAllActive().stream()
                .sorted((c1, c2) -> {
                    String c1Name = c1.getName() + ' ' + c1.getOtherClientInfo();
                    String c2Name = c2.getName() + ' ' + c2.getOtherClientInfo();
                    return c1Name.compareTo(c2Name);
                })
                .collect(toList());

        if (null != vacationId) {
            model.addAttribute("vacationModel", vacationService.findById(vacationId));
        } else {
            VacationDto vacationDto = new VacationDto();

            if (startDate != null) {
                vacationDto.setArrivalDate(startDate);
            }
            if (endDate != null) {
                vacationDto.setLeaveDate(endDate);
            }
            if (rooms != null && !rooms.isEmpty()) {
                vacationDto.setRoomNumbers(rooms);
            }
            if (sharedRooms != null && !sharedRooms.isEmpty()) {
                vacationDto.setHasSharedRooms(true);
                vacationDto.setSharedRoomNumbers(sharedRooms);
            }

            model.addAttribute("vacationModel", vacationDto);
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
    public String listClients(Model model) {
        model.addAttribute("vacations", vacationService.getAllActiveVacations());
        return "listVacations.html";
    }

    @GetMapping("/vacation/{id}/approve")
    @ResponseStatus(HttpStatus.OK)
    public void approveVacation(@PathVariable(name = "id") Long id,
                                @RequestParam(name = "approval") Boolean approval) {
        vacationService.approveVacation(id, approval);
    }


    @RequestMapping(path = "/vacation/{vacId}/remove", method = RequestMethod.GET)
    public String removeVacation(@PathVariable Long vacId) {
        log.debug("vacId={}", vacId);
        vacationService.inactivateVacation(vacId);
        log.debug("vacId={}", vacId);
        return "redirect:/vacations";
    }

    @GetMapping(path = "/vacation/{vacId}/remove-ajax")
    @ResponseStatus(HttpStatus.OK)
    public void removeVacationAjax(@PathVariable Long vacId) {
        vacationService.inactivateVacation(vacId);
    }


    @RequestMapping(value = "/vacations/paginated", method = RequestMethod.GET)
    @ResponseBody
    public String listClientsPaginated(HttpServletRequest request, HttpServletResponse response, Model model) {

        DataTableRequest<Vacation> dataTableInRQ = new DataTableRequest<>(request);
        PaginationCriteria pagination = dataTableInRQ.getPaginationRequest();

        String baseQuery = "SELECT '#'||vacation.id as vacid, client.name||client.other_client_info as clientname, client.id as clientid, to_char(vacation.arrival_date,'YYYY-MM-DD') as startdate," +
                " to_char(vacation.leave_date,'YYYY-MM-DD') as enddate, array_to_string(array_agg(room.room_number),', ') as roomnumbers,(SELECT COUNT(1) FROM vacation) AS totalrecords FROM vacation" +
                " JOIN client ON client.id = vacation.client_id" +
                " JOIN room_vacation ON vacation.id = room_vacation.vac_id" +
                " JOIN room ON room.id = room_vacation.room_id" +
                " GROUP BY 1,2,3,4,5,7";
        String paginatedQuery = AppUtil.buildPaginatedQuery(baseQuery, pagination);

        System.out.println(paginatedQuery);

        Query query = entityManager.createNativeQuery(paginatedQuery, VacationModel.class);

        @SuppressWarnings("unchecked")
        List<VacationModel> vacationList = query.getResultList();

        DataTableResults<VacationModel> dataTableResult = new DataTableResults<>();
        dataTableResult.setDraw(dataTableInRQ.getDraw());
        dataTableResult.setListOfDataObjects(vacationList);
        if (!AppUtil.isObjectEmpty(vacationList)) {
            dataTableResult.setRecordsTotal(vacationList.get(0).getTotalrecords()
                    .toString());
            if (dataTableInRQ.getPaginationRequest().isFilterByEmpty()) {
                dataTableResult.setRecordsFiltered(vacationList.get(0).getTotalrecords()
                        .toString());
            } else {
                dataTableResult.setRecordsFiltered(Integer.toString(vacationList.size()));
            }
        }
        return new Gson().toJson(dataTableResult);
    }
}
