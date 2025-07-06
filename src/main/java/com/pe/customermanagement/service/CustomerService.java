package com.pe.customermanagement.service;

import com.pe.customermanagement.entity.Customer;
import com.pe.customermanagement.excepcion.InternalErrorException;
import com.pe.customermanagement.excepcion.NotFoundException;
import com.pe.customermanagement.mapper.CustomerMapper;
import com.pe.customermanagement.model.CreateCustomerResponse;
import com.pe.customermanagement.model.CustomerRequest;
import com.pe.customermanagement.model.CustomerResponse;
import com.pe.customermanagement.model.HeaderRequest;
import com.pe.customermanagement.repository.ICustomerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService implements ICustomerService{
    private final ICustomerRepository customerRepository;
    private final TraceProducerService traceProducerService;
    private final CustomerMapper customerMapper;

    @Override
    public Mono<CreateCustomerResponse> createCustomer(CustomerRequest customerRequest, HeaderRequest header) {
        Customer customer = customerMapper.fromRequest(customerRequest);
        Mono<Customer> customerMono = customerRepository.save(customer);
        return customerMono
                .map(customerMapper::toResponse)
                //.map()
                .onErrorResume(ex -> Mono.error(new InternalErrorException("Error creating customer", ex)));
    }

    @Override
    public Mono<CreateCustomerResponse> updateCustomer(String id, CustomerRequest customerRequest, HeaderRequest header) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer not found")))
                .flatMap(existingCustomer -> {
                    Customer updatedCustomer = customerMapper.fromRequest(customerRequest);
                    updatedCustomer.setId(existingCustomer.getId());
                    return customerRepository.save(updatedCustomer);
                })
                .map(customerMapper::toResponse);
    }

    @Override
    public Mono<Page<CustomerResponse>> getAllCustomers(int page, int size, HeaderRequest header) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);


        log.info("Fetching customers with page: {}, size: {}", page, size);
        return customerRepository.findAllBy(pageable)
                .doOnNext(customer -> log.info("Customer found: {}", customer))
                .map(customerMapper::toCustomerResponse)
                .switchIfEmpty(Mono.error(new NotFoundException("No customers found")))
                .collectList()
                .zipWith(customerRepository.count())
                .map(tuple -> {;
                    long totalElements = tuple.getT2();
                    return new PageImpl<>(tuple.getT1(), pageable, totalElements);
                });

    }

    @Override
    public Mono<CustomerResponse> getCustomerById(String id, HeaderRequest header) {
        return customerRepository.findById(id)
                .map(customerMapper::toCustomerResponse)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer not found")));
    }

    @Override
    public Flux<CustomerResponse> getAllCustomersv2(int page, int size, HeaderRequest header) {

        return  customerRepository.findAll()
                .map(customerMapper::toCustomerResponse)
                .switchIfEmpty(Mono.error(new NotFoundException("No customers found")));
    }

}
