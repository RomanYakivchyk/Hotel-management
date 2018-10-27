package com.demo.hotel_management.controller;

import com.demo.hotel_management.entity.RoomVacation;
import com.demo.hotel_management.repository.RoomVacationRepository;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

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
    public List<List> getTableData(@RequestParam(value = "initDateString", required = false) String initDateString) throws JSONException {
        // date format is the following: 2018-10-18
        initMonthLengths(initDateString);
        return buildTable();
    }

    private void initMonthLengths(String initDateString) {
        LocalDate initLocalDate = LocalDate.now();
        if (initDateString != null) {
            initLocalDate = LocalDate.parse(initDateString);
        }
        currMonth = initLocalDate;
        nextMonth = initLocalDate.plusMonths(1);
    }


    private List<List> buildTable() {

        List<RoomVacation> roomVacationList = roomVacationRepository
                .findVacsBetween(LocalDate.of(currMonth.getYear(), currMonth.getMonth(), 1),
                        LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), nextMonth.lengthOfMonth()));

        splitVacsToSubLists(roomVacationList);

        List<List> tableRows = new ArrayList<>();

        tableRows.add(buildTableHeader());

        for (int roomNum = 1; roomNum <= ROOM_COUNT; roomNum++) {

            List<Map> topSubRowJSON = buildSubRow(roomNum, topSubRowVacations);
            List<Map> bottomSubRowJSON = buildSubRow(roomNum, bottomSubRowVacations);

            tableRows.add(topSubRowJSON);
            tableRows.add(bottomSubRowJSON);
        }
        return tableRows;
    }

    private List<Map> buildTableHeader() {

        List<Map> headerList = new ArrayList<>();
        for (LocalDate ld : Arrays.asList(currMonth, nextMonth)) {
            for (int i = 1; i <= ld.lengthOfMonth(); i++) {
                Map<String, String> headerCellMap = new HashMap<>();
                LocalDate currDay = LocalDate.of(ld.getYear(), ld.getMonth(), i);
                String dayOfWeek = currDay.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("uk-UA"));
                headerCellMap.put("dayOfMonth", String.valueOf(i));
                headerCellMap.put("dayOfWeek", dayOfWeek);
                headerCellMap.put("monthName", currDay.getMonth().getDisplayName(TextStyle.FULL, Locale.forLanguageTag("uk-UA")));
                headerCellMap.put("monthLength", String.valueOf(currDay.lengthOfMonth()));
                headerList.add(headerCellMap);
            }
        }
        return headerList;
    }

    private List<Map> buildSubRow(int roomNumber, List<RoomVacation> subRow) {

        List<Map> subRowList = new ArrayList<>();
        for (int i = 1; i < currMonth.lengthOfMonth() + nextMonth.lengthOfMonth() + 1; i++) {
            LocalDate day = findDayOfMonth(i);
            Map<String, String> cellJSON = buildCell(roomNumber, day, subRow);
            subRowList.add(cellJSON);
        }
        return subRowList;
    }

    private Map<String, String> buildCell(int roomNumber, LocalDate day, List<RoomVacation> subRow) {

        List<RoomVacation> roomVacations = roomVacationsInDay(day, roomNumber, subRow);

        Map<String, String> cellMap = new HashMap<>();


        for (int i = 0; i < roomVacations.size(); i++) {

            LocalDate arrivalDate = roomVacations.get(i).getVacation().getVacationDate().getArrivalDate();
            LocalDate leaveDate = roomVacations.get(i).getVacation().getVacationDate().getLeaveDate();

            boolean isArrivalDate = arrivalDate.getDayOfMonth() == day.getDayOfMonth();
            boolean isLeaveDate = leaveDate.getDayOfMonth() == day.getDayOfMonth();


            long vacId = roomVacations.get(i).getVacation().getId();
            String clientName = roomVacations.get(i).getVacation().getClient().getName();

            cellMap.put("vac_" + i, String.valueOf(vacId));
            cellMap.put("room_" + i, String.valueOf(roomNumber));
            cellMap.put("client_" + i, clientName);
            cellMap.put("arrival_" + i, String.valueOf(isArrivalDate));
            cellMap.put("leave_" + i, String.valueOf(isLeaveDate));
            cellMap.put("start_" + i, String.valueOf(arrivalDate));
            cellMap.put("end_" + i, String.valueOf(leaveDate));
        }
        return cellMap;
    }


    private void splitVacsToSubLists(List<RoomVacation> list) {

        topSubRowVacations.clear();
        bottomSubRowVacations.clear();

        List<RoomVacation> roomVacationList = list.stream()
                .filter(RoomVacation::getAllowRoommate)
                .collect(Collectors.toList());


        list.stream()
                .filter(RoomVacation::getAllowRoommate)
                .forEach(e -> splitVacsToSubLists(e, roomVacationList));

        list.stream()
                .filter(e -> !e.getAllowRoommate())
                .forEach(e -> {
                    topSubRowVacations.add(e);
                    bottomSubRowVacations.add(e);
                });
    }

    private void splitVacsToSubLists(RoomVacation rv, List<RoomVacation> list) {
        list.remove(rv);
        List<RoomVacation> listCopy = new ArrayList<>(list);

        if (!topSubRowVacations.contains(rv) && !bottomSubRowVacations.contains(rv)) {
            topSubRowVacations.add(rv);
        }

        list.forEach(e -> {
            if (twoVacationOverlap(rv, e)) {
                if (topSubRowVacations.contains(rv) && !bottomSubRowVacations.contains(e)) {
                    bottomSubRowVacations.add(e);
                } else if (bottomSubRowVacations.contains(rv) && !topSubRowVacations.contains(e)) {
                    topSubRowVacations.add(e);
                }
                if (listCopy.size() > 1)
                    splitVacsToSubLists(e, listCopy);
            }
        });
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


    private List<RoomVacation> roomVacationsInDay(LocalDate day, Integer roomNumber, List<RoomVacation> list) {

        return list.stream()
                .filter(e -> {
                    LocalDate arrival = e.getVacation().getVacationDate().getArrivalDate();
                    LocalDate leave = e.getVacation().getVacationDate().getLeaveDate();

                    return day.equals(arrival) || day.equals(leave)
                            || (day.isAfter(arrival) && day.isBefore(leave));

                })
                .filter(e -> e.getRoom().getRoomNumber().equals(roomNumber))
                .sorted((rv1, rv2) -> {
                    LocalDate arrDate1 = rv1.getVacation().getVacationDate().getArrivalDate();
                    LocalDate arrDate2 = rv2.getVacation().getVacationDate().getArrivalDate();
                    return (!arrDate1.equals(arrDate2)) ? (arrDate1.isAfter(arrDate2)) ? 1 : -1 : 0;
                })
                .collect(toList());
    }

}
