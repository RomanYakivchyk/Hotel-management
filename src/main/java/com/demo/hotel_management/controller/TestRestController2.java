package com.demo.hotel_management.controller;

import com.demo.hotel_management.entity.RoomVacation;
import com.demo.hotel_management.repository.RoomVacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@RestController
public class TestRestController2 {

    @Autowired
    RoomVacationRepository roomVacationRepository;

    public static final Integer ROOM_COUNT = 9; //todo

    private List<RoomVacation> topSubRowVacations = new ArrayList<>();
    private List<RoomVacation> bottomSubRowVacations = new ArrayList<>();

    private LocalDate currMonth;
    private LocalDate nextMonth;


    @GetMapping("/tableData")
    public String getTableData(@RequestParam(value = "initDateString", required = false) String initDateString) {
        // date format is the following: 2018-10-18
        initMonthLengths(initDateString);

        StringBuilder htmlTable = new StringBuilder();
        String tableHeader = buildTableHeader();
        htmlTable.append(tableHeader);
        String tableBody = buildTableBody();
        htmlTable.append(tableBody);
        return htmlTable.toString();
    }

    private void initMonthLengths(String initDateString) {
        LocalDate initLocalDate = LocalDate.now();
        if (initDateString != null) {
            initLocalDate = LocalDate.parse(initDateString);
        }
        currMonth = initLocalDate;
        nextMonth = initLocalDate.plusMonths(1);
    }


    private String buildTableHeader() {
        StringBuilder sb = new StringBuilder();
        sb.append("<thead><tr>");
        for (int i = 0; i < currMonth.lengthOfMonth() + 1; i++) {
            if (i == 0) {
//                sb.append("<th class='text-center headcol bg-light text-black' style='height:26.5px;'></th>");
            } else {
                sb.append("<th class='text-center bg-secondary'>").append(i).append("</th>");
            }
        }
        for (int i = 1; i <= nextMonth.lengthOfMonth(); i++) {
            if (i == 1) {
                sb.append("<th class='text-center bg-secondary' style=\"border-left: 4px solid #18BC9C;\">").append(i).append("</th>");
            } else {
                sb.append("<th class='text-center bg-secondary'>").append(i).append("</th>");
            }
        }
        sb.append("</tr></thead>");
        return sb.toString();
    }

    private boolean dateRangesOverlap(LocalDate start1, LocalDate end1, LocalDate start2, LocalDate end2) {
        return start1.isBefore(end2) && end1.isAfter(start2);
    }

    private boolean twoVacationOverlap(RoomVacation roomVacation1, RoomVacation roomVacation2) {

        LocalDate startDate1 = roomVacation1.getVacation().getVacationDate().getArrivalDate();
        LocalDate endDate1 = roomVacation1.getVacation().getVacationDate().getLeaveDate();


        LocalDate startDate2 = roomVacation2.getVacation().getVacationDate().getArrivalDate();
        LocalDate endDate2 = roomVacation2.getVacation().getVacationDate().getLeaveDate();

        return dateRangesOverlap(startDate1, endDate1, startDate2, endDate2);
    }

    private void splitVacsToSubLists(List<RoomVacation> list) {

        list.stream()
                .filter(e -> !e.getAllowRoommate())
                .filter(e -> !topSubRowVacations.contains(e))
                .filter(e -> !bottomSubRowVacations.contains(e))
                .forEach(e -> {
                    topSubRowVacations.add(e);
                    bottomSubRowVacations.add(e);
                });

        List<RoomVacation> roomVacationList = list.stream()
                .filter(RoomVacation::getAllowRoommate)
                .collect(toList());

        list.stream()
                .filter(RoomVacation::getAllowRoommate)
                .filter(e -> !topSubRowVacations.contains(e))
                .filter(e -> !bottomSubRowVacations.contains(e))
                .forEach(e -> splitVacsToSubLists(e, roomVacationList));

    }

    private void splitVacsToSubLists(RoomVacation rv, List<RoomVacation> list) {
        list.remove(rv);
        List<RoomVacation> listCopy = new ArrayList<>(list);

        if (!topSubRowVacations.contains(rv) && !bottomSubRowVacations.contains(rv)) {
            topSubRowVacations.add(rv);
        }

        list.forEach(e -> {
            if (twoVacationOverlap(rv, e)) {
                if (topSubRowVacations.contains(rv)) {
                    bottomSubRowVacations.add(e);
                } else if (bottomSubRowVacations.contains(rv)) {
                    topSubRowVacations.add(e);
                }
                if (listCopy.size() > 1)
                    splitVacsToSubLists(e, listCopy);
            }
        });
    }


    private String buildTableBody() {

        List<RoomVacation> roomVacationList = roomVacationRepository
                .findVacsBetween(LocalDate.of(currMonth.getYear(), currMonth.getMonth(), 1),
                        LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), nextMonth.lengthOfMonth()));

        splitVacsToSubLists(roomVacationList);

        StringBuilder sb = new StringBuilder();
        sb.append("<tbody>");

        Supplier<Stream<RoomVacation>> topRowStreamSupplier = () -> topSubRowVacations.stream();
        Supplier<Stream<RoomVacation>> bottomRowStreamSupplier = () -> bottomSubRowVacations.stream();

        for (int roomNum = 1; roomNum <= ROOM_COUNT; roomNum++) {

            String topSubRow = buildSubRow(roomNum, topSubRowVacations);
            String bottomSubRow = buildSubRow(roomNum, bottomSubRowVacations);

            sb.append(topSubRow);
            sb.append(bottomSubRow);
        }
        sb.append("</tbody>");
        return sb.toString();
    }


    private LocalDate findDayOfMonth(int counter) {
        int dayOfMonth;
        LocalDate date = currMonth;
        if (counter > currMonth.lengthOfMonth()) {
            date = date.plusMonths(1);
            dayOfMonth = counter % currMonth.lengthOfMonth();
        } else {
            dayOfMonth = counter;
        }
        return LocalDate.of(date.getYear(), date.getMonth(), dayOfMonth);
    }

    private String buildSubRow(int roomNumber, List<RoomVacation> subRow) {
        StringBuilder sb = new StringBuilder();
        sb.append("<tr>");
        for (int j = 1; j < currMonth.lengthOfMonth() + nextMonth.lengthOfMonth() + 1; j++) { // j = 0
//            if (j == 0) {
//                sb.append("<th class='text-center headcol bg-light text-black'>").append(roomNumber).append("</th>");
//            }
//            else {
            LocalDate day = findDayOfMonth(j);
            String cell = buildCell(roomNumber, day, subRow);
//                String cell = "<td class='text-center'></td>";
            sb.append(cell);
//            }
        }
        sb.append("</tr>");
        return sb.toString();
    }


    private List<RoomVacation> roomVacationsInDay(LocalDate day, Integer roomNumber, List<RoomVacation> list) {

        return list.stream()
                .filter(e -> {
                    LocalDate arrival = e.getVacation().getVacationDate().getArrivalDate();
                    LocalDate leave = e.getVacation().getVacationDate().getLeaveDate();

                    return day.equals(arrival) || day.equals(leave)
                            || (day.isAfter(arrival) && day.isBefore(leave));

                })
                .filter(e -> e.getRoom().getRoomNumber().equals(roomNumber))
                .collect(toList());
    }

    private String buildCell(int roomNumber, LocalDate day, List<RoomVacation> subRow) {

        StringBuilder sb = new StringBuilder();

        List<RoomVacation> roomVacations = roomVacationsInDay(day, roomNumber, subRow);

        if (roomVacations.size() != 0) {

            sb.append("<td class='text-center bg-white' ");
            for (int i = 0; i < roomVacations.size(); i++) {
                boolean isArrivalDate = roomVacations.get(i).getVacation().getVacationDate().getArrivalDate().getDayOfMonth() == day.getDayOfMonth();
                boolean isLeaveDate = roomVacations.get(i).getVacation().getVacationDate().getLeaveDate().getDayOfMonth() == day.getDayOfMonth();

                sb.append(String.format("data-vac_%d=\"%d\" ", i, roomVacations.get(i).getVacation().getId()));
                sb.append(String.format("data-room_%d=\"%d\" ", i, roomNumber));
                sb.append(String.format("data-client_%d=\"%s\" ", i, roomVacations.get(i).getVacation().getClient().getName()));
                sb.append(String.format("data-arrival_%d=\"%s\" ", i, String.valueOf(isArrivalDate)));
                sb.append(String.format("data-leave_%d=\"%s\" ", i, String.valueOf(isLeaveDate)));
            }
            sb.append("><span style='text-align:center;line-height:16px;' class='bg-info btn-block'>" + roomVacations.get(0).getVacation().getClient().getName() + "</span></td>");

        } else {
            sb.append(String.format("<td class='text-center'>%s</td>", "")); //todo replace X
        }

        return sb.toString();
    }
}
