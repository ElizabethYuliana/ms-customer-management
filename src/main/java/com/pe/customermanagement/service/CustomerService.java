package com.pe.customermanagement.service;


import com.pe.customermanagement.dto.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface CustomerService {

    Mono<CustomerResponse> createCustomer(CustomerRequest customerRequest, AuditContext context);
    Mono<CustomerResponse> updateCustomer(String id, CustomerRequest customerRequest, AuditContext context);
    Mono<CustomerResponsePage> getAllCustomers(int page, int size, AuditContext context);
    Mono<CustomerResponse> getCustomerById(String id, AuditContext context);
}
