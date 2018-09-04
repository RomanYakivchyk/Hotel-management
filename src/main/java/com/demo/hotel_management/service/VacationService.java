package com.demo.hotel_management.service;

import com.demo.hotel_management.dto.ClientDto;
import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.entity.Vacation;
import com.demo.hotel_management.exceptions.ClientNotFoundException;
import com.demo.hotel_management.repository.ClientRepository;
import com.demo.hotel_management.repository.VacationRepository;
import com.demo.hotel_management.utils.EntityDtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
public class VacationService {

    @Autowired
    private EntityDtoConverter entityDtoConverter;

    @Autowired
    private VacationRepository vacationRepository;

    public VacationDto findById(Long vacationId) {
        log.debug("vacationId={}", vacationId);
        Vacation foundVacation = vacationRepository.findById(vacationId).orElseThrow(NoSuchElementException::new);
        log.debug("vacationId={}, foundVacation={}", vacationId, foundVacation);
        return entityDtoConverter.convertVacationEntityToDto(foundVacation);
    }


    public VacationDto saveVacation(VacationDto vacationDto) {

        log.debug("vacationDto={}", vacationDto);

        Vacation vacation = entityDtoConverter.convertVacationDtoToEntity(vacationDto);
        Vacation savedVacation = vacationRepository.save(vacation);

        log.debug("savedVacation={}", savedVacation);

        return entityDtoConverter.convertVacationEntityToDto(savedVacation);
    }

    public void removeVacation(Long vacationId) {

        log.debug("vacationId={}", vacationId);

        Vacation vacation = vacationRepository.findById(vacationId).orElseThrow(NoSuchElementException::new);
        vacationRepository.delete(vacation);
        log.debug("Vacation removed: vacation={}", vacation);

    }

}
