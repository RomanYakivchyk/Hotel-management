//package com.demo.hotel_management.controller;
//
//import com.demo.hotel_management.dto.VacationDto;
//import com.demo.hotel_management.entity.Vacation;
//import com.demo.hotel_management.service.VacationService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//import java.io.FileNotFoundException;
//import java.io.PrintWriter;
//import java.time.LocalDate;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Slf4j
//@Controller
//public class TestRestController {
//
//    @Autowired
//    VacationService vacationService;
//
//    public static final Integer ROOM_COUNT = 9; //todo
//
////    @GetMapping("/tableData")
////    @ResponseBody
////    public String getTableData() {
////        LocalDate currDate = LocalDate.now();  //todo
////        return createTable(currDate);
////    }
//
//    public String createTable(LocalDate date) {
//
//        StringBuilder htmlTable = new StringBuilder();
//        initTableHeader(htmlTable, date.lengthOfMonth());
//        populateTableBody(htmlTable, date, date.lengthOfMonth(), ROOM_COUNT);
//        return htmlTable.toString();
//    }
//
//    private void populateTableBody(StringBuilder sb, LocalDate date, Integer monthLength, Integer roomCount) {
//        List<VacationDto> vacationDtoList = vacationService.getVacationsTable(date);
//        StringBuilder tableSB = new StringBuilder();
//        tableSB.append("<tbody>");
//        for (int i = 1; i <= roomCount; i++) {
//            for (int k = 0; k < 2; k++) {
//                StringBuilder rowSB = new StringBuilder();
//                rowSB.append("<tr>");
//                for (int j = 0; j < monthLength + 1; j++) {
//                    String cell;
//                    if (j == 0) {
//                        cell = "<th scope=\"row\">" + i + "</th>";
//                    } else {
//                        LocalDate currentDay = LocalDate.of(date.getYear(), date.getMonth(), j);
//                        Integer roomNumber = i;
//                        cell = createCell(roomNumber, currentDay, vacationDtoList, k);
//                    }
//                    rowSB.append(cell);
//                }
//                rowSB.append("</tr>");
//                tableSB.append(rowSB);
//            }
//        }
//        tableSB.append("</tbody>");
//        sb.append(tableSB.toString());
//    }
//
//
//    private void initTableHeader(StringBuilder sb, Integer monthLength) {
//        sb.append("<thead><tr>");
//        for (int i = 0; i < monthLength + 1; i++) {
//            if (i == 0) {
//                sb.append("<th></th>");
//            } else {
//                sb.append("<th scope=\"col\">" + i + "</th>");
//            }
//        }
//        sb.append("</tr></thead>");
//    }
//
//    private String createCell(Integer roomNumber, LocalDate date, List<VacationDto> vacationDtoList, int k) {
//
//        List<VacationDto> vacationsInDay = vacationDtoList.stream()
//                .filter(e -> e.getRoomNumbers().contains(roomNumber))
//                .filter(e -> (date.isAfter(e.getArrivalDate()) && date.isBefore(e.getLeaveDate()))
//                        || date.isEqual(e.getArrivalDate()) || date.isEqual(e.getLeaveDate()))
//                .collect(Collectors.toList());
//
//        StringBuilder cellSB = new StringBuilder();
//
//        if (vacationsInDay.size() != 0) {
//
//            VacationDto vacationDto = null;
//            if (vacationsInDay.size() == 1) {
//                vacationDto = vacationsInDay.get(0);
//            } else if (vacationsInDay.size() == 2) {
//                vacationDto = vacationsInDay.get(k);
//            }
//            boolean isArrivalDate = vacationDto.getArrivalDate().getDayOfMonth() == date.getDayOfMonth();
//            boolean isLeaveDate = vacationDto.getLeaveDate().getDayOfMonth() == date.getDayOfMonth();
//
//            cellSB.append(String.format("<td data-vac=\"%d\" ", vacationDto.getVacationId()));
//            cellSB.append(String.format("data-room=\"%d\" ", roomNumber));
//            cellSB.append(String.format("data-client=\"%s\" ", vacationDto.getClientName()));
//            cellSB.append(String.format("data-arrival=\"%s\" ", String.valueOf(isArrivalDate)));
//            cellSB.append(String.format("data-leave=\"%s\">", String.valueOf(isLeaveDate)));
//            cellSB.append(String.format("%s</td>", "X")); //todo replace X
//        } else {
//            cellSB.append(String.format("<td>%s</td>", "X")); //todo replace X
//        }
//
//        return cellSB.toString();
//    }
//
//    @GetMapping("/bookingTable")
//    public String showTable() {
//        return "calendarTest2.html";
//    }
//
////    @GetMapping("/tableData")
////    @ResponseBody
////    public Map<String, List<Map<String, String>>> getTableData(
//////            @RequestParam //remove required false
//////                    LocalDate date
////    ) throws FileNotFoundException {
////
////        LocalDate date = LocalDate.now();
////        int rowCount = 8;//todo remove hardcode
////        int monthLength = date.lengthOfMonth();
////
////
////        String htmlTable = "";
////        PrintWriter writer = new PrintWriter(htmlTable);
////        writer.write("<table class=\"table table-sm\" id=\"bookingTable\">");
////        initTableHeader(writer, monthLength);
////
////
////        List<VacationDto> vacationDtoList = vacationService.getVacationsTable(date);
////
////        Map<String, List<Map<String, String>>> rows = new LinkedHashMap<>();
////        for (int i = 1; i <= rowCount; i++) {
////            for (int k = 0; k < 2; k++) {
////                List<Map<String, String>> row = new ArrayList<>();
////                for (int j = 1; j <= monthLength; j++) {
////                    LocalDate currentDay = LocalDate.of(date.getYear(), date.getMonth(), j);
////                    Integer roomNumber = i;
////                    createCell(roomNumber, currentDay, vacationDtoList, k);
////                    row.add(cell);
////                }
////                rows.put(String.format("row_%s.%s", i, (k + 1)), row);
////            }
////        }
////        return rows;
////    }
//
//}
