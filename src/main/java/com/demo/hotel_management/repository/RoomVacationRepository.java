package com.demo.hotel_management.repository;

import com.demo.hotel_management.entity.RoomVacation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RoomVacationRepository extends JpaRepository<RoomVacation, Long> {

    @Query(value = "FROM RoomVacation WHERE vac_id=:vacId")
    List<RoomVacation> findByVacationId(@Param("vacId") Long vacId);

    @Query(value = "SELECT rv.* FROM room_vacation AS rv INNER JOIN vacation AS vac ON (rv.vac_id = vac.id)" +
            "WHERE vac.inactive=false AND ?1 <= vac.leave_date AND ?2 >= vac.arrival_date"
            , nativeQuery = true)
    List<RoomVacation> findVacsBetween(LocalDate from, LocalDate to);
}
