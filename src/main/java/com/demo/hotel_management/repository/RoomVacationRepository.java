package com.demo.hotel_management.repository;

import com.demo.hotel_management.entity.RoomVacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomVacationRepository extends JpaRepository<RoomVacation, Long> {

    @Query("FROM RoomVacation WHERE vac_id=:vacId")
    List<RoomVacation> findByVacationId(@Param("vacId")Long vacId);
}
