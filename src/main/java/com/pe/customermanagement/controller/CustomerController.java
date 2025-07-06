package com.pe.customermanagement.controller;

import com.pe.customermanagement.dto.*;
import com.pe.customermanagement.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor @Slf4j
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public Mono<ResponseEntity<CreateCustomerResponse>> createCustomer(
            @Valid @RequestBody CustomerRequest customerRequest,
            @RequestHeader(value = "consumerId") String consumeId,
            @RequestHeader(value = "traceparent") String traceParent,
            @RequestHeader(value = "deviceType") String deviceType,
            @RequestHeader(value = "deviceId") String deviceId,
            ServerWebExchange exchange) {

        return customerService.createCustomer(customerRequest, exchange).map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CreateCustomerResponse>> updateCustomer(
            @PathVariable String id,
            @RequestBody CustomerRequest customerRequest,
            @RequestHeader(value = "consumerId") String consumeId,
            @RequestHeader(value = "traceparent") String traceParent,
            @RequestHeader(value = "deviceType") String deviceType,
            @RequestHeader(value = "deviceId") String deviceId,
            ServerWebExchange exchange) {
        HeaderRequest header = new HeaderRequest(consumeId, traceParent, deviceType, deviceId);
        return customerService.updateCustomer(id, customerRequest, exchange).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerResponse>> getCustomerById(
            @PathVariable String id,
            @RequestHeader(value = "consumerId") String consumeId,
            @RequestHeader(value = "traceparent") String traceParent,
            @RequestHeader(value = "deviceType") String deviceType,
            @RequestHeader(value = "deviceId") String deviceId,
            ServerWebExchange exchange) {

        HeaderRequest header = new HeaderRequest(consumeId, traceParent, deviceType, deviceId);
        return customerService.getCustomerById(id, exchange)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Mono<ResponseEntity<CustomerResponsePage>> getAllCustomers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestHeader(value = "consumerId") String consumeId,
            @RequestHeader(value = "traceparent") String traceParent,
            @RequestHeader(value = "deviceType") String deviceType,
            @RequestHeader(value = "deviceId") String deviceId,
            ServerWebExchange exchange) {
        HeaderRequest header = new HeaderRequest(consumeId, traceParent, deviceType, deviceId);
        return customerService.getAllCustomers(page, size, exchange).map(ResponseEntity::ok);
    }

}
