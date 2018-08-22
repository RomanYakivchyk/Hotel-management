package com.demo.hotel_management.repository;

import com.demo.hotel_management.entity.Vacation;
import org.springframework.data.repository.CrudRepository;

public interface VacationRepository extends CrudRepository<Vacation,Long> {

}
