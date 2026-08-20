package com.pe.customermanagement.service.impl;

import com.pe.customermanagement.dto.*;
import com.pe.customermanagement.entity.Customer;
import com.pe.customermanagement.excepcion.NotFoundException;
import com.pe.customermanagement.mapper.CustomerMapper;
import com.pe.customermanagement.repository.CustomerRepository;
import com.pe.customermanagement.service.CustomerService;
import com.pe.customermanagement.validator.RequiredHeaderValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import static com.pe.customermanagement.common.Constant.*;

/**
 * CustomerServiceImpl is the implementation of the CustomerService interface.
 * It provides methods to create, update, retrieve, and list customers.
 * It uses CustomerRepository for database operations and CustomerMapper for data transformation.
 *
 * @author Elizabeth Valdez
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository   customerRepository;
    private final CustomerMapper       customerMapper;

    /**
     * Creates a new customer based on the provided CustomerRequest.
     * Validates the context headers before proceeding with the creation.
     *
     * @param request  the CustomerRequest containing customer details
     * @param context  the AuditContext for validation
     * @return a Mono containing the created CustomerResponse
     */
    @Override
    public Mono<CustomerResponse> createCustomer(CustomerRequest request, AuditContext context) {
        return RequiredHeaderValidator.validate(context)
                .then(Mono.just(request))
                .map(customerMapper::fromRequest)
                .flatMap(customerRepository::save)
                .map(customerMapper::toCustomerResponse);
    }

    /**
     * Updates an existing customer identified by the given ID with the provided CustomerRequest.
     * Validates the context headers before proceeding with the update.
     *
     * @param id       the ID of the customer to update
     * @param request  the CustomerRequest containing updated customer details
     * @param context  the AuditContext for validation
     * @return a Mono containing the updated CustomerResponse
     */
    @Override
    public Mono<CustomerResponse> updateCustomer(String id, CustomerRequest request, AuditContext context) {
        return RequiredHeaderValidator.validate(context)
                .then(customerRepository.findById(id))
                .switchIfEmpty(Mono.error(new NotFoundException(String.format(NOT_FOUND_ERROR_MESSAGE, id))))
                .flatMap(existingCustomer -> updateExistingCustomer(existingCustomer, request))
                .map(customerMapper::toCustomerResponse);
    }

    /**
     * Retrieves all customers with pagination support.
     * Validates the context headers before proceeding with the retrieval.
     *
     * @param page    the page number to retrieve
     * @param size    the number of customers per page
     * @param context the AuditContext for validation
     * @return a Mono containing a CustomerResponsePage with the list of customers
     */
    @Override
    public Mono<CustomerResponsePage> getAllCustomers(int page, int size, AuditContext context) {
        Pageable pageable = PageRequest.ofSize(size).withPage(page);
        return RequiredHeaderValidator.validate(context)
                .thenMany(customerRepository.findAllBy(pageable))
                .map(customerMapper::toCustomerResponse)
                .switchIfEmpty(Mono.error(new NotFoundException(CUSTOMER_NOT_FOUND)))
                .collectList()
                .zipWith(customerRepository.count())
                .map(tuple ->  new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()))
                .map(customerMapper::toCustomerResponsePage);
    }

    /**
     * Retrieves a customer by its ID.
     * Validates the context headers before proceeding with the retrieval.
     *
     * @param id      the ID of the customer to retrieve
     * @param context the AuditContext for validation
     * @return a Mono containing the CustomerResponse if found, or an error if not found
     */
    @Override
    public Mono<CustomerResponse> getCustomerById(String id, AuditContext context) {
        return RequiredHeaderValidator.validate(context)
                .then(customerRepository.findById(id))
                .map(customerMapper::toCustomerResponse)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format(NOT_FOUND_ERROR_MESSAGE, id))));
    }

    /**
     * Updates an existing customer with the details from the provided CustomerRequest.
     *
     * @param existingCustomer the existing customer to update
     * @param request          the CustomerRequest containing updated customer details
     * @return a Mono containing the updated Customer entity
     */
    private Mono<Customer> updateExistingCustomer(Customer existingCustomer, CustomerRequest request) {
        Customer updatedCustomer = customerMapper.fromRequest(request);
        updatedCustomer.setId(existingCustomer.getId());
        return customerRepository.save(updatedCustomer);
    }

}
