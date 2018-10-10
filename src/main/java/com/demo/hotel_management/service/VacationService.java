package com.demo.hotel_management.service;

import com.demo.hotel_management.dto.VacationDto;
import com.demo.hotel_management.entity.RoomVacation;
import com.demo.hotel_management.entity.Vacation;
import com.demo.hotel_management.repository.RoomVacationRepository;
import com.demo.hotel_management.repository.VacationRepository;
import com.demo.hotel_management.utils.EntityDtoConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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

    public void removeVacation(Long vacationId) {

        log.debug("vacationId={}", vacationId);

        Vacation vacation = vacationRepository.findById(vacationId).orElseThrow(NoSuchElementException::new);
        vacationRepository.delete(vacation);
        log.debug("Vacation removed: vacation={}", vacation);

    }

    //todo add sorting
    public Page<VacationDto> findPaginated(Pageable pageable) {
        log.debug("pageable={}", pageable);

        Collator collator = Collator.getInstance(Locale.forLanguageTag("uk-UA"));

        List<VacationDto> vacationDtoList = StreamSupport.stream(vacationRepository.findAll().spliterator(), false)
                .map(entityDtoConverter::convertVacationEntityToDto)
                //todo
//                .sorted((o1, o2) -> {
//                    int result = 0;
//                    result = collator.compare(o1.get(), o2.getName());
//                    if (result == 0) {
//                        result = collator.compare(o1.getOtherClientInfo(), o2.getOtherClientInfo());
//                    }
//                    return result;
//                })
                .collect(Collectors.toCollection(ArrayList::new));

        log.debug("pageable={}", vacationDtoList);

        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<VacationDto> list;

        if (vacationDtoList.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, vacationDtoList.size());
            list = vacationDtoList.subList(startItem, toIndex);
        }

        log.debug("pageSize={}, currentPage={}, startItem={}, List<VacationDto>={} ", pageSize, currentPage, startItem, list);

        return new PageImpl<>(list, PageRequest.of(currentPage, pageSize), vacationDtoList.size());
    }

    public List<VacationDto> getAllActiveVacations() {
        return StreamSupport.stream(vacationRepository.findAll().spliterator(), false)
                .filter(e -> !e.getInactive())
                .map(e -> entityDtoConverter.convertVacationEntityToDto(e))
                .collect(Collectors.toList());
    }


    public List<VacationDto> getVacationsTable(LocalDate date) {
        List<Vacation> vacationList = vacationRepository
                .findByMonth(LocalDate.of(date.getYear(), date.getMonth(), 1),
                        LocalDate.of(date.getYear(), date.getMonth(), date.lengthOfMonth()));
        return vacationList.stream()
                .map(entityDtoConverter::convertVacationEntityToDto)
                .collect(Collectors.toList());
    }
}