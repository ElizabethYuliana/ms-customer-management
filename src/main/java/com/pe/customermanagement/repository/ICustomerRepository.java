package com.pe.customermanagement.repository;

import com.pe.customermanagement.entity.Customer;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepository extends ReactiveMongoRepository<String, Customer> {

}
