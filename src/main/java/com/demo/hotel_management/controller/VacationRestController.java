package com.demo.hotel_management.controller;

import com.demo.hotel_management.entity.RoomVacation;
import com.demo.hotel_management.repository.RoomVacationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

import static java.util.stream.Collectors.toList;

@RestController
public class VacationRestController {

    @Autowired
    private RoomVacationRepository roomVacationRepository;
    @Value("${vac.room.count}")
    private Integer ROOM_COUNT; //todo

    private List<RoomVacation> topSubRowVacations = new ArrayList<>();
    private List<RoomVacation> bottomSubRowVacations = new ArrayList<>();

    private LocalDate currMonth;
    private LocalDate nextMonth;

    @GetMapping("/tableData/{monthDirection}")
    public List<List> getTableData(@PathVariable String monthDirection,
                                   @CookieValue(value = "initDateString", required = false) String initDateString,
                                   HttpServletResponse response) {
        // date format is the following: 2018-10-18
        LocalDate initLocalDate = LocalDate.now();

        if (initDateString == null) {
            initDateString = initLocalDate.toString();
        }

        switch (monthDirection) {
            case "curr":
                initLocalDate = LocalDate.now();
                break;
            case "prev":
                initLocalDate = LocalDate.parse(initDateString).minusMonths(1);
                break;
            case "next":
                initLocalDate = LocalDate.parse(initDateString).plusMonths(1);
                break;
        }
        Cookie cookie = new Cookie("initDateString", initLocalDate.toString());
        response.addCookie(cookie);

        currMonth = initLocalDate;
        nextMonth = initLocalDate.plusMonths(1);

        return buildTable();
    }

    private List<List> buildTable() {

        List<RoomVacation> roomVacationList = roomVacationRepository
                .findVacsBetween(LocalDate.of(currMonth.getYear(), currMonth.getMonth(), 1),
                        LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), nextMonth.lengthOfMonth()))
                .stream()
                .sorted((rv1, rv2) -> {
                    LocalDate arrDate1 = rv1.getVacation().getVacationDate().getArrivalDate();
                    LocalDate arrDate2 = rv2.getVacation().getVacationDate().getArrivalDate();
                    return (!arrDate1.equals(arrDate2)) ? (arrDate1.isAfter(arrDate2)) ? 1 : -1 : 0;
                }).collect(toList());

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
                headerCellMap.put("monthName", currDay.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.forLanguageTag("uk-UA")));
                headerCellMap.put("monthLength", String.valueOf(currDay.lengthOfMonth()));
                headerCellMap.put("date", currDay.toString());
                if(currDay.equals(LocalDate.now())){
                    headerCellMap.put("today", "true");
                }
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

            boolean isArrivalDate = arrivalDate.equals(day);
            boolean isLeaveDate = leaveDate.equals(day);


            long vacId = roomVacations.get(i).getVacation().getId();
            String clientName = roomVacations.get(i).getVacation().getClient().getName();
            Boolean approved = roomVacations.get(i).getVacation().getApproved();

            cellMap.put("vac_" + i, String.valueOf(vacId));
//            cellMap.put("room_" + i, String.valueOf(roomNumber));
            cellMap.put("client_" + i, clientName);
            cellMap.put("arrival_" + i, String.valueOf(isArrivalDate));
            cellMap.put("leave_" + i, String.valueOf(isLeaveDate));
            cellMap.put("start_" + i, String.valueOf(arrivalDate));
            cellMap.put("end_" + i, String.valueOf(leaveDate));
//            cellMap.put("day_" + i, String.valueOf(day.getDayOfMonth()));
            cellMap.put("approved_" + i, String.valueOf(approved));
        }
        return cellMap;
    }


    private void splitVacsToSubLists(List<RoomVacation> list) {

        topSubRowVacations.clear();
        bottomSubRowVacations.clear();

        list.stream()
                .filter(e -> !e.getAllowRoommate())
                .forEach(e -> {
                    topSubRowVacations.add(e);
                    bottomSubRowVacations.add(e);
                });

        list.stream()
                .filter(RoomVacation::getAllowRoommate)
                .sorted((o1, o2) -> {
                    LocalDate o1ArrivalDate = o1.getVacation()
                            .getVacationDate()
                            .getArrivalDate();

                    LocalDate o2ArrivalDate = o2.getVacation()
                            .getVacationDate()
                            .getArrivalDate();
                    return o1ArrivalDate.isEqual(o2ArrivalDate) ? 0 : (o1ArrivalDate.isBefore(o2ArrivalDate) ? -1 : 1);
                })
                .forEach(e -> {

                    LocalDate previousDateTop = getPreviousRoomVac(topSubRowVacations, e);
                    LocalDate previousDateBottom = getPreviousRoomVac(bottomSubRowVacations, e);

                    if (previousDateTop != null) {
                        if (previousDateBottom != null) {
                            if (previousDateTop.isAfter(previousDateBottom)) {
                                addElementToSubRow(topSubRowVacations, bottomSubRowVacations, e);
                            } else {
                                addElementToSubRow(bottomSubRowVacations, topSubRowVacations, e);
                            }
                        } else {
                            addElementToSubRow(topSubRowVacations, bottomSubRowVacations, e);
                        }
                    } else {
                        if (previousDateBottom != null) {
                            addElementToSubRow(bottomSubRowVacations, topSubRowVacations, e);
                        } else {
                            addElementToSubRow(topSubRowVacations, bottomSubRowVacations, e);
                        }
                    }
                });
    }

    private void addElementToSubRow(List<RoomVacation> roomVacationList1,
                                    List<RoomVacation> roomVacationList2,
                                    RoomVacation roomVacation) {
        if (noOverlaps(roomVacationList1, roomVacation)) {
            roomVacationList1.add(roomVacation);
        } else if (noOverlaps(roomVacationList2, roomVacation)) {
            roomVacationList2.add(roomVacation);
        }
    }

    private LocalDate getPreviousRoomVac(List<RoomVacation> subRow, RoomVacation fromRoomVac) {
        LocalDate fromRoomVacArrivalDate = fromRoomVac.getVacation().getVacationDate().getArrivalDate();
        RoomVacation rv = subRow.stream()
                .sorted((o1, o2) -> {
                    LocalDate o1LeaveDate = o1.getVacation()
                            .getVacationDate()
                            .getLeaveDate();

                    LocalDate o2LeaveDate = o2.getVacation()
                            .getVacationDate()
                            .getLeaveDate();
                    return o1LeaveDate.isEqual(o2LeaveDate) ? 0 : (o1LeaveDate.isAfter(o2LeaveDate) ? -1 : 1);
                })
                .filter(vac -> vac.getRoom().getRoomNumber().equals(fromRoomVac.getRoom().getRoomNumber()))
                .filter(vac -> vac.getVacation().getVacationDate().getLeaveDate().isBefore(fromRoomVacArrivalDate)
                        || vac.getVacation().getVacationDate().getLeaveDate().isEqual(fromRoomVacArrivalDate)
                )
                .findFirst()
                .orElse(null);

        if (rv == null)
            return null;
        else
            return rv.getVacation().getVacationDate().getLeaveDate();
    }

    private boolean noOverlaps(List<RoomVacation> roomVacationList, RoomVacation rv) {
        return roomVacationList.stream()
                .filter(e -> e.getRoom().getRoomNumber().equals(rv.getRoom().getRoomNumber()))
                .noneMatch(e -> roomVacOverlap(e, rv));
    }

    private boolean roomVacOverlap(RoomVacation rv1, RoomVacation rv2) {
        LocalDate start1 = rv1.getVacation().getVacationDate().getArrivalDate();
        LocalDate end1 = rv1.getVacation().getVacationDate().getLeaveDate();
        LocalDate start2 = rv2.getVacation().getVacationDate().getArrivalDate();
        LocalDate end2 = rv2.getVacation().getVacationDate().getLeaveDate();
        return start1.isBefore(end2) && end1.isAfter(start2);
    }


    private LocalDate findDayOfMonth(int counter) {
        int dayOfMonth;
        LocalDate date = currMonth;
        if (counter > currMonth.lengthOfMonth()) {
            date = date.plusMonths(1);
            dayOfMonth = counter - currMonth.lengthOfMonth();

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
