package com.demo.hotel_management.repository;

import com.demo.hotel_management.entity.Client;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ClientRepository extends PagingAndSortingRepository<Client,Long> {

}
