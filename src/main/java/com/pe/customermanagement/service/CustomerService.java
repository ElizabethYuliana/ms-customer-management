package com.pe.customermanagement.service;


import com.pe.customermanagement.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface CustomerService {

    Mono<CreateCustomerResponse> createCustomer(CustomerRequest customerRequest, ServerWebExchange exchange);
    Mono<CreateCustomerResponse> updateCustomer(String id, CustomerRequest customerRequest, ServerWebExchange exchange);
    Mono<CustomerResponsePage> getAllCustomers(int page, int size, ServerWebExchange exchange);
    Mono<CustomerResponse> getCustomerById(String id, ServerWebExchange exchange);
}
