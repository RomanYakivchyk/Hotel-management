package com.demo.hotel_management.repository;

import com.demo.hotel_management.entity.Vacation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface VacationRepository extends PagingAndSortingRepository<Vacation, Long> {

    @Query("FROM Vacation WHERE NOT ((arrival_date < :from AND leave_date < :from) " +
            "OR (arrival_date > :to AND leave_date > :to))")
    List<Vacation> findByMonth(@Param("from")LocalDate from,@Param("to") LocalDate to);
}

