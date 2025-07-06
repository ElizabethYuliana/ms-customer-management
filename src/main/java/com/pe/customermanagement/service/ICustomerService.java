package com.pe.customermanagement.service;


import com.pe.customermanagement.model.CreateCustomerResponse;
import com.pe.customermanagement.model.CustomerRequest;
import com.pe.customermanagement.model.CustomerResponse;
import com.pe.customermanagement.model.HeaderRequest;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface ICustomerService {

    Mono<CreateCustomerResponse> createCustomer(CustomerRequest customerRequest, HeaderRequest header);
    Mono<CreateCustomerResponse> updateCustomer(String id, CustomerRequest customerRequest, HeaderRequest header);
    Mono<Page<CustomerResponse>> getAllCustomers(int page, int size, HeaderRequest header);
    Mono<CustomerResponse> getCustomerById(String id,HeaderRequest header);
    Flux<CustomerResponse> getAllCustomersv2(int page, int size, HeaderRequest header);
}
