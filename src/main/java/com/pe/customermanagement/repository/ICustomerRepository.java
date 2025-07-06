package com.pe.customermanagement.repository;

import com.pe.customermanagement.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;


@Repository
public interface ICustomerRepository extends ReactiveMongoRepository<Customer, String> {
    Flux<Customer> findAllBy(Pageable pageable);

}