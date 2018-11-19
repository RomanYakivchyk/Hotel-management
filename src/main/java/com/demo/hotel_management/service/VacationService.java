package com.demo.hotel_management.service;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.Client;
import com.demo.hotel_management.entity.RoomVacation;
import com.demo.hotel_management.entity.Vacation;
import com.demo.hotel_management.exceptions.ClientNotFoundException;
import com.demo.hotel_management.repository.RoomVacationRepository;
import com.demo.hotel_management.repository.VacationRepository;
import com.demo.hotel_management.utils.EntityDtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class VacationService {

    @Autowired
    private EntityDtoConverter entityDtoConverter;

    @Autowired
    private VacationRepository vacationRepository;

    @Autowired
    private RoomVacationRepository roomVacationRepository;

    public VacationDto findById(Long vacationId) {
        log.debug("vacationId={}", vacationId);
        Vacation foundVacation = vacationRepository.findById(vacationId).orElseThrow(NoSuchElementException::new);
        log.debug("vacationId={}, foundVacation={}", vacationId, foundVacation);
        return entityDtoConverter.convertVacationEntityToDto(foundVacation);
    }

    //todo
    public VacationDto saveVacation(VacationDto vacationDto) {

        log.debug("vacationDto={}", vacationDto);

        Vacation vacation = entityDtoConverter.convertVacationDtoToEntity(vacationDto);
        Vacation savedVacation = vacationRepository.save(vacation);

        log.debug("savedVacation={}", savedVacation);
        return entityDtoConverter.convertVacationEntityToDto(savedVacation);
    }

    public void inactivateVacation(Long vacationId) {

        log.debug("vacationId={}", vacationId);
        Vacation vacation = vacationRepository.findById(vacationId).orElseThrow(ClientNotFoundException::new);
        vacationRepository.inactivate(vacationId);
        log.debug("vacation inactivated: vacation={}", vacation);

    }

    public Page<VacationDto> findAllPageable(Pageable pageable) {
        List<VacationDto> vacationDtoList = getAllActiveVacations();

        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > vacationDtoList.size() ? vacationDtoList.size() : (start + pageable.getPageSize());

        return new PageImpl<>(vacationDtoList.subList(start, end), pageable, vacationDtoList.size());
    }


    public List<VacationDto> getAllActiveVacations() {
        return vacationRepository.findByInactiveFalse().stream()
                .map(entityDtoConverter::convertVacationEntityToDto)
                .collect(toList());
    }

    public void approveVacation(Long id, Boolean approval) {
        vacationRepository.approve(id, approval);
    }

    public List<VacationDto> getVacationsForTomorrow() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        return vacationRepository.findByInactiveFalse().stream()
                .filter(e -> e.getVacationDate().getArrivalDate().equals(tomorrow))
                .map(e -> entityDtoConverter.convertVacationEntityToDto(e))
                .collect(toList());
    }
}