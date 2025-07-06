package com.pe.customermanagement.service.impl;

import com.pe.customermanagement.common.LogUtil;
import com.pe.customermanagement.dto.*;
import com.pe.customermanagement.entity.Customer;
import com.pe.customermanagement.excepcion.InternalErrorException;
import com.pe.customermanagement.excepcion.NotFoundException;
import com.pe.customermanagement.mapper.CustomerMapper;
import com.pe.customermanagement.repository.ICustomerRepository;
import com.pe.customermanagement.service.CustomerService;
import com.pe.customermanagement.service.EventProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


import java.util.Map;

import static com.pe.customermanagement.common.Constant.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    private final ICustomerRepository  customerRepository;
    private final EventProducerService eventProducerService;
    private final CustomerMapper       customerMapper;

    @Override
    public Mono<CreateCustomerResponse> createCustomer(CustomerRequest request, ServerWebExchange exchange) {
        return customerRepository.save(customerMapper.fromRequest(request))
                .map(customer -> buildCustomerResponse(request, customer, exchange))
                .doOnNext(eventProducerService::sendAuditMessage)
                .doOnSuccess(LogUtil::buildLogging)
                .map(AuditContext::getResponse)
                .onErrorResume(ex -> Mono.error(new InternalErrorException(INTERNAL_ERROR_MESSAGE, ex)));
    }


    @Override
    public Mono<CreateCustomerResponse> updateCustomer(String id, CustomerRequest request, ServerWebExchange exchange) {
        return customerRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException(String.format(NOT_FOUND_ERROR_MESSAGE, id))))
                .flatMap(existingCustomer -> updateExistingCustomer(existingCustomer, request))
                .map(customer -> buildCustomerResponse(request, customer, exchange))
                .doOnNext(eventProducerService::sendAuditMessage)
                .doOnSuccess(LogUtil::buildLogging)
                .map(AuditContext::getResponse);
    }

    @Override
    public Mono<CustomerResponsePage> getAllCustomers(int page, int size, ServerWebExchange exchange) {
        Pageable pageable = PageRequest.ofSize(size).withPage(page).withSort(Sort.by(Sort.Direction.ASC, "name"));

        return customerRepository.findAllBy(pageable)
                .map(customerMapper::toCustomerResponse)
                .switchIfEmpty(Mono.error(new NotFoundException(CUSTOMER_NOT_FOUND)))
                .collectList()
                .zipWith(customerRepository.count())
                .map(tuple ->  new PageImpl<>(tuple.getT1(), pageable, tuple.getT2()))
                .map(c -> {
                    CustomerResponsePage response = customerMapper.toCustomerResponsePage(c);
                    return LogUtil.buildAuditContext(Map.of(page, size), response, new Customer(), exchange);
                })
                .doOnNext(eventProducerService::sendAuditMessage)
                .doOnSuccess(LogUtil::buildLogging)
                .map(AuditContext::getResponse);

    }

    @Override
    public Mono<CustomerResponse> getCustomerById(String id, ServerWebExchange exchange) {
        return customerRepository.findById(id)
                .map(c -> {
                    CustomerResponse response = customerMapper.toCustomerResponse(c);
                    return LogUtil.buildAuditContext(id, response, c, exchange);
                })
                .switchIfEmpty(Mono.error(new NotFoundException(String.format(NOT_FOUND_ERROR_MESSAGE, id))))
                .doOnNext(eventProducerService::sendAuditMessage)
                .doOnSuccess(LogUtil::buildLogging)
                .map(AuditContext::getResponse);
    }


    private Mono<Customer> updateExistingCustomer(Customer existingCustomer, CustomerRequest request) {
        Customer updatedCustomer = customerMapper.fromRequest(request);
        updatedCustomer.setId(existingCustomer.getId());
        return customerRepository.save(updatedCustomer);
    }

    private AuditContext<CustomerRequest, CreateCustomerResponse> buildCustomerResponse(CustomerRequest request,
                                                                                        Customer customer,
                                                                                        ServerWebExchange exchange) {
        CreateCustomerResponse response = customerMapper.toResponse(customer);
        return LogUtil.buildAuditContext(request, response, customer, exchange);
    }

}
