package com.pe.customermanagement.service.impl;

import com.pe.customermanagement.common.LogUtil;
import com.pe.customermanagement.dto.AuditContext;
import com.pe.customermanagement.dto.CustomerRequest;
import com.pe.customermanagement.dto.CustomerResponse;
import com.pe.customermanagement.dto.CustomerResponsePage;
import com.pe.customermanagement.mapper.AuditContextMapper;
import com.pe.customermanagement.service.CustomerService;
import com.pe.customermanagement.service.EventProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


import static com.pe.customermanagement.common.Constant.SUCCESS_CODE;


/**
 * CustomerServiceAudited is a decorator for the CustomerService that adds auditing capabilities.
 * It intercepts the methods of CustomerService to log and send events related to customer operations.
 *
 * @author Elizabeth Valdez
 * @version 1.0
 */
@Component
@Primary
@RequiredArgsConstructor
@Slf4j
public class CustomerServiceAudited implements CustomerService {

    private final CustomerService      delegate;
    private final AuditContextMapper   auditContextMapper;
    private final EventProducerService eventProducerService;

    /**
     * Creates a new customer and logs the operation.
     *
     * @param request  the CustomerRequest containing customer details
     * @param context  the AuditContext for logging and event sending
     * @return a Mono containing the created CustomerResponse
     */
    @Override
    public Mono<CustomerResponse> createCustomer(CustomerRequest request, AuditContext context) {
        return delegate.createCustomer(request, context)
                .map(response -> auditContextMapper.buildCustomerResponse(response, context))
                .doOnSuccess(this::sendEvent)
                .doOnSuccess(this::auditLog)
                .map(AuditContext::getResponse);
    }

    /**
     * Updates an existing customer identified by the given ID and logs the operation.
     *
     * @param id       the ID of the customer to update
     * @param request  the CustomerRequest containing updated customer details
     * @param context  the AuditContext for logging and event sending
     * @return a Mono containing the updated CustomerResponse
     */
    @Override
    public Mono<CustomerResponse> updateCustomer(String id, CustomerRequest request, AuditContext context) {
        return delegate.updateCustomer(id, request, context)
                .map(response -> auditContextMapper.buildCustomerResponse(response, context))
                .doOnSuccess(this::sendEvent)
                .doOnSuccess(this::auditLog)
                .map(AuditContext::getResponse);
    }

    /**
     * Retrieves all customers with pagination and logs the operation.
     *
     * @param page    the page number to retrieve
     * @param size    the number of customers per page
     * @param context the AuditContext for logging and event sending
     * @return a Mono containing a CustomerResponsePage with the requested customers
     */
    @Override
    public Mono<CustomerResponsePage> getAllCustomers(int page, int size, AuditContext context) {
        return delegate.getAllCustomers(page, size, context)
                .map(response -> auditContextMapper.buildCustomerResponsePage(response, context))
                .doOnSuccess(this::sendEvent)
                .doOnSuccess(this::auditLog)
                .map(AuditContext::getResponsePage);
    }

    /**
     * Retrieves a customer by ID and logs the operation.
     *
     * @param id       the ID of the customer to retrieve
     * @param context  the AuditContext for logging and event sending
     * @return a Mono containing the CustomerResponse for the requested customer
     */
    @Override
    public Mono<CustomerResponse> getCustomerById(String id, AuditContext context) {
        return delegate.getCustomerById(id, context)
                .map(response -> auditContextMapper.buildCustomerResponse(response, context))
                .doOnSuccess(this::sendEvent)
                .doOnSuccess(this::auditLog)
                .map(AuditContext::getResponse);
    }

    private void sendEvent(AuditContext context) {
        eventProducerService.sendEvent(auditContextMapper.buildCustomerTraceEvent(context, SUCCESS_CODE)).subscribe();
    }

    private void auditLog(AuditContext context) {
        LogUtil.buildLogging(context);
    }
}
