package com.demo.hotel_management.repository;

import com.demo.hotel_management.entity.Vacation;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface VacationRepository extends PagingAndSortingRepository<Vacation,Long> {

}
