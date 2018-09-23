package com.demo.hotel_management.controller;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.Vacation;
import com.demo.hotel_management.service.VacationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class TestRestController {

    @Autowired
    VacationService vacationService;

    @GetMapping("/tableData")
    @ResponseBody
    public Map<String, List<Map<String, String>>> getTableData(
//            @RequestParam //remove required false
//                    LocalDate date
    ) {

//        Integer month1Length = date.lengthOfMonth();
//        Integer month2Length = date.plusMonths(1).lengthOfMonth();


//        vacationService.findByMonth(monthNumber);
//
//
//        List<Row> rows = new ArrayList<>();
//        Row row = new Row();
//        row.setCells();
        LocalDate date = LocalDate.now();
        int rowCount = 8;//todo remove hardcode
        int monthLength = date.lengthOfMonth();
        List<VacationDto> vacationDtoList = vacationService.getVacationsTable(date);
        vacationDtoList.forEach(System.out::println);
//        log.debug("vacationDtoList size={}", vacationDtoList.size());
        Map<String, List<Map<String, String>>> rows = new LinkedHashMap<>();
        for (int i = 1; i <= rowCount; i++) {
            for (int k = 0; k < 2; k++) {
                List<Map<String, String>> row = new ArrayList<>();
                for (int j = 1; j <= monthLength; j++) {
                    LocalDate currentDay = LocalDate.of(date.getYear(), date.getMonth(), j);
                    Integer roomNumber = i;
                    Map<String, String> cell = new LinkedHashMap<>(vacationIdForCell(roomNumber, currentDay, vacationDtoList, k));
                    row.add(cell);
                }
                rows.put(String.format("row_%s.%s", i, (k + 1)), row);
            }
        }
        return rows;
    }

    private Map<String, String> vacationIdForCell(Integer roomNumber, LocalDate date,
                                                  List<VacationDto> vacationDtoList, int k) {


        List<VacationDto> vacationsInDay = vacationDtoList.stream()
                .filter(e -> e.getRoomNumbers().contains(roomNumber))
                .filter(e -> (date.isAfter(e.getArrivalDate()) && date.isBefore(e.getLeaveDate()))
                        || date.isEqual(e.getArrivalDate()) || date.isEqual(e.getLeaveDate()))
                .collect(Collectors.toList());

        Map<String, String> vacationIdsMap = new LinkedHashMap<>();
        VacationDto vacationDto = null;
        if (vacationsInDay.size() != 0) {

            if (vacationsInDay.size() == 1) {
                vacationDto = vacationsInDay.get(0);
            } else if (vacationsInDay.size() == 2) {
                vacationDto = vacationsInDay.get(k);
            }
            vacationIdsMap.put("vacId", String.valueOf(vacationDto.getVacationId()));
            vacationIdsMap.put("room", String.valueOf(roomNumber));
            vacationIdsMap.put("client", String.valueOf(vacationDto.getClientName()));
            boolean isArrivalDay = vacationDto.getArrivalDate().getDayOfMonth() == date.getDayOfMonth();
            vacationIdsMap.put("arrival", String.valueOf(isArrivalDay));
            boolean isLeaveDay = vacationDto.getLeaveDate().getDayOfMonth() == date.getDayOfMonth();
            vacationIdsMap.put("leave", String.valueOf(isLeaveDay));
        }
        return vacationIdsMap;
    }


    @GetMapping("/bookingTable")
    public String showTable() {
        System.out.println("showTable()");
        return "calendarTest2.html";
    }

}
