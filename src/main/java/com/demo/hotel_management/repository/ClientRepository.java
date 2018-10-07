package com.demo.hotel_management.repository;

import com.demo.hotel_management.entity.Client;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface ClientRepository extends PagingAndSortingRepository<Client,Long> {

    @Modifying
    @Transactional
    @Query(value = "update Client c set c.inactive = true where c.id = :client_id")
    void inactivate( @Param("client_id") Long clientId);
}
