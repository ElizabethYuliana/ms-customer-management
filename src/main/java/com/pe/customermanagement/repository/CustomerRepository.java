package com.pe.customermanagement.repository;

import com.pe.customermanagement.entity.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

/**
 * CustomerRepository interface for managing Customer entities in MongoDB.
 * It extends ReactiveMongoRepository to provide reactive data access methods.
 *
 * @author Elizabeth Valdez
 * @version 1.0
 */
@Repository
public interface CustomerRepository extends ReactiveMongoRepository<Customer, String> {
    Flux<Customer> findAllBy(Pageable pageable);

}