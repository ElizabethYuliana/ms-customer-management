package com.pe.customermanagement.controller;

import com.pe.customermanagement.dto.*;
import com.pe.customermanagement.service.ICustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerService customerService;

    @PostMapping
    public Mono<ResponseEntity<CreateCustomerResponse>> createCustomer(
            @Valid @RequestBody CustomerRequest customerRequest,
            @RequestHeader(value = "consumerId") String consumeId,
            @RequestHeader(value = "traceparent") String traceParent,
            @RequestHeader(value = "deviceType") String deviceType,
            @RequestHeader(value = "deviceId") String deviceId) {
        HeaderRequest header = new HeaderRequest(consumeId, traceParent, deviceType, deviceId);
        return customerService.createCustomer(customerRequest, header).map(ResponseEntity::ok);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CreateCustomerResponse>> updateCustomer(
            @PathVariable String id,
            @RequestBody CustomerRequest customerRequest,
            @RequestHeader(value = "consumerId") String consumeId,
            @RequestHeader(value = "traceparent") String traceParent,
            @RequestHeader(value = "deviceType") String deviceType,
            @RequestHeader(value = "deviceId") String deviceId) {
        HeaderRequest header = new HeaderRequest(consumeId, traceParent, deviceType, deviceId);
        return customerService.updateCustomer(id, customerRequest, header).map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerResponse>> getCustomerById(
            @PathVariable String id,
            @RequestHeader(value = "consumerId") String consumeId,
            @RequestHeader(value = "traceparent") String traceParent,
            @RequestHeader(value = "deviceType") String deviceType,
            @RequestHeader(value = "deviceId") String deviceId) {

        HeaderRequest header = new HeaderRequest(consumeId, traceParent, deviceType, deviceId);
        return customerService.getCustomerById(id, header)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Mono<ResponseEntity<Mono<Page<CustomerResponse>>>> getAllCustomers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestHeader(value = "consumerId") String consumeId,
            @RequestHeader(value = "traceparent") String traceParent,
            @RequestHeader(value = "deviceType") String deviceType,
            @RequestHeader(value = "deviceId") String deviceId) {
        HeaderRequest header = new HeaderRequest(consumeId, traceParent, deviceType, deviceId);
        return Mono.just(ResponseEntity.ok(customerService.getAllCustomers(page, size, header)));
    }

    @GetMapping("/v2")
    public Mono<ResponseEntity<Flux<CustomerResponse>>> getAllCustomersv2(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "1") int size,
            @RequestHeader(value = "consumerId") String consumeId,
            @RequestHeader(value = "traceparent") String traceParent,
            @RequestHeader(value = "deviceType") String deviceType,
            @RequestHeader(value = "deviceId") String deviceId) {
        HeaderRequest header = new HeaderRequest(consumeId, traceParent, deviceType, deviceId);
        return Mono.just(customerService.getAllCustomersv2(page, size, header))
                .map(ResponseEntity::ok);
    }
}
