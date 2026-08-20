package com.pe.customermanagement.service;

import com.pe.customermanagement.dto.AuditContext;
import com.pe.customermanagement.dto.CustomerRequest;
import com.pe.customermanagement.dto.CustomerResponse;
import com.pe.customermanagement.dto.CustomerResponsePage;
import com.pe.customermanagement.entity.Customer;
import com.pe.customermanagement.excepcion.BadRequestException;
import com.pe.customermanagement.excepcion.NotFoundException;
import com.pe.customermanagement.mapper.CustomerMapper;
import com.pe.customermanagement.repository.CustomerRepository;
import com.pe.customermanagement.service.impl.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer customerEntity;
    private CustomerRequest customerRequest;
    private CustomerResponse customerResponse;
    private AuditContext auditContext;

    @BeforeEach
    void setup() {
        customerRequest = buildCustomerRequest();
        customerEntity = buildCustomer();
        customerResponse = buildCustomerResponse();
        auditContext = buildAuditContext();
    }

    @Test
    void createCustomer_shouldFail_whenRequiredHeadersMissing() {
        CustomerRequest request = buildCustomerRequest();
        Map<String, String> headers = Map.of("consumerId", "web");
        AuditContext context = new AuditContext();
        context.setHeaders(headers);

        StepVerifier.create(customerService.createCustomer(request, context))
                .expectError(BadRequestException.class)
                .verify();
    }


    @Test
    void createCustomer_shouldSucceed() {
        when(customerMapper.fromRequest(customerRequest)).thenReturn(customerEntity);
        when(customerRepository.save(customerEntity)).thenReturn(Mono.just(customerEntity));
        when(customerMapper.toCustomerResponse(customerEntity)).thenReturn(customerResponse);

        StepVerifier.create(customerService.createCustomer(customerRequest, auditContext))
                .expectNextMatches(resp -> resp.fullName().equalsIgnoreCase("Luis Ramirez Perez") &&
                                                            resp.id().equals("65as4d5a4sd54d"))
                .verifyComplete();
    }

    @Test
    void updateCustomer_shouldSucceed() {
        when(customerRepository.findById("123")).thenReturn(Mono.just(customerEntity));
        when(customerMapper.fromRequest(customerRequest)).thenReturn(customerEntity);
        when(customerRepository.save(any())).thenReturn(Mono.just(customerEntity));
        when(customerMapper.toCustomerResponse(any())).thenReturn(customerResponse);

        StepVerifier.create(customerService.updateCustomer("123", customerRequest, auditContext))
                .expectNextMatches(resp -> resp.fullName().contains("Luis Ramirez Perez"))
                .verifyComplete();
    }

    @Test
    void updateCustomer_notFound_shouldError() {
        when(customerRepository.findById("noexist")).thenReturn(Mono.empty());

        StepVerifier.create(customerService.updateCustomer("noexist", customerRequest, auditContext))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void getCustomerById_shouldSucceed() {
        when(customerRepository.findById("123")).thenReturn(Mono.just(customerEntity));
        when(customerMapper.toCustomerResponse(any())).thenReturn(customerResponse);

        StepVerifier.create(customerService.getCustomerById("123", auditContext))
                .expectNextCount(1)
                .verifyComplete();
    }

    @Test
    void getCustomerById_notFound_shouldError() {
        when(customerRepository.findById("noexist")).thenReturn(Mono.empty());

        StepVerifier.create(customerService.getCustomerById("noexist", auditContext))
                .expectError(NotFoundException.class)
                .verify();
    }

    @Test
    void getAllCustomers_shouldSucceed() {
        Pageable pageable = PageRequest.of(0, 1);
        when(customerRepository.findAllBy(pageable)).thenReturn(Flux.just(customerEntity));
        when(customerRepository.count()).thenReturn(Mono.just(1L));
        when(customerMapper.toCustomerResponse(any())).thenReturn(customerResponse);
        when(customerMapper.toCustomerResponsePage(any())).thenAnswer(i -> {
            Page<CustomerResponse> page = i.getArgument(0);
            return new CustomerResponsePage(
                    page.getNumber() + 1,
                    page.getSize(),
                    page.getTotalPages(),
                    (int) page.getTotalElements(),
                    page.getContent()
            );
        });

        StepVerifier.create(customerService.getAllCustomers(0, 1, auditContext))
                .expectNextMatches(page -> page.customers().size() == 1 )
                .verifyComplete();
    }


    AuditContext buildAuditContext() {
        Map<String, String> headers = Map.of(
                "consumerId", "web",
                "traceparent", "abc-123",
                "deviceType", "IOS",
                "deviceId", "xyz-456");

        return AuditContext.builder()
                .headers(headers)
                .build();
    }

    Customer buildCustomer() {
        Customer customerEntity = new Customer();
        customerEntity.setId("65as4d5a4sd54d");
        customerEntity.setName("Luis");
        customerEntity.setFirstLastName("Ramirez");
        customerEntity.setSecondLastName("Perez");
        customerEntity.setStatus("Active");
        return customerEntity;
    }

    CustomerRequest buildCustomerRequest() {
        return new CustomerRequest("Luis", "Ramirez", "Perez", "Active");
    }

    CustomerResponse buildCustomerResponse() {
        return new CustomerResponse("65as4d5a4sd54d", "Luis Ramirez Perez");
    }

}
