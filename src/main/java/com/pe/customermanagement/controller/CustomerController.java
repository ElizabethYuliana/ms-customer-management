package com.pe.customermanagement.controller;

import com.pe.customermanagement.dto.*;
import com.pe.customermanagement.mapper.AuditContextMapper;
import com.pe.customermanagement.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService    customerService;
    private final AuditContextMapper auditContextMapper;

    @PostMapping
    public Mono<ResponseEntity<CustomerResponse>> createCustomer(
            @Valid @RequestBody CustomerRequest request, ServerWebExchange exchange) {
        AuditContext context = auditContextMapper.buildRequestAuditContext(request, exchange);
        return customerService.createCustomer(request, context).map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerResponse>> updateCustomer(
            @PathVariable String id,
            @RequestBody CustomerRequest request,
            ServerWebExchange exchange) {
        AuditContext context = auditContextMapper.buildRequestAuditContext(request, exchange);
        return customerService.updateCustomer(id, request, context).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerResponse>> getCustomerById(
            @PathVariable String id,
            ServerWebExchange exchange) {
        AuditContext context = auditContextMapper.buildAuditContext(exchange);
        return customerService.getCustomerById(id, context)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Mono<ResponseEntity<CustomerResponsePage>> getAllCustomers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "1") int size,
            ServerWebExchange exchange) {
        AuditContext context = auditContextMapper.buildAuditContext(exchange);
        return customerService.getAllCustomers(page, size, context).map(ResponseEntity::ok);
    }

}
