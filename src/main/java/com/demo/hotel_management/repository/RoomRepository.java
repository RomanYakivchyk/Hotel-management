package com.demo.hotel_management.repository;

import com.demo.hotel_management.entity.Room;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
@Repository
public interface RoomRepository extends CrudRepository<Room, Long> {

//    @Query("FROM Room where room_number IN :roomNumbers")
//    List<Room> findByRoomNumberIn(@Param("roomNumbers") Set<Integer> roomNumbers);
}
