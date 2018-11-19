package com.demo.hotel_management.viber;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.service.VacationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public class NotificationSender {

    @Autowired
    private VacationService vacationService;

    @Scheduled(fixedDelay = 3000)
    public void todaysVacationNotification() {

        List<VacationDto> todaysVacationList = vacationService.getVacationsForTomorrow();


    }
//
//    @Scheduled(fixedDelay = 3000)
//    public void needApproveNotification() {
//
//        List<VacationDto> todaysVacationList = vacationService.getVacationsForToday();
//
//
//    }
}
