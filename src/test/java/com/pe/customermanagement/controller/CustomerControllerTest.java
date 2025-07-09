package com.pe.customermanagement.controller;

import com.pe.customermanagement.dto.CustomerRequest;
import com.pe.customermanagement.dto.CustomerResponse;
import com.pe.customermanagement.dto.CustomerResponsePage;
import com.pe.customermanagement.mapper.AuditContextMapper;
import com.pe.customermanagement.service.CustomerService;
import com.pe.customermanagement.service.EventProducerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(CustomerController.class)
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private CustomerService customerService;

    @MockitoBean
    private EventProducerService eventProducerService;

    @MockitoBean
    private AuditContextMapper auditContextMapper;

    private CustomerRequest customerRequest;
    private CustomerResponse customerResponse;

    @BeforeEach
    void setup() {
        customerRequest = new CustomerRequest("Elizabeth", "Valdez", "Lopez", "Active");
        customerResponse = new CustomerResponse("as64a654da4", "Elizabeth Valdez Lopez");
    }

    @Test
    void shouldCreateCustomer() {
        when(customerService.createCustomer(any(CustomerRequest.class), any())).thenReturn(Mono.just(customerResponse));

        webTestClient.post()
                .uri("/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("as64a654da4")
                .jsonPath("$.fullName").isEqualTo("Elizabeth Valdez Lopez");
    }

    @Test
    void shouldUpdateCustomer() {
        when(customerService.updateCustomer(eq("as64a654da4"), any(CustomerRequest.class), any()))
                .thenReturn(Mono.just(customerResponse));

        webTestClient.put()
                .uri("/customer/as64a654da4")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(customerRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("as64a654da4")
                .jsonPath("$.fullName").isEqualTo("Elizabeth Valdez Lopez");
    }

    @Test
    void shouldGetCustomerById() {
        when(customerService.getCustomerById(eq("as64a654da4"), any()))
                .thenReturn(Mono.just(customerResponse));

        webTestClient.get()
                .uri("/customer/as64a654da4")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.id").isEqualTo("as64a654da4");
    }

    @Test
    void shouldGetAllCustomers() {
        CustomerResponsePage page = new CustomerResponsePage(1, 1, 1, 1, List.of(customerResponse));

        when(customerService.getAllCustomers(eq(1), eq(1), any()))
                .thenReturn(Mono.just(page));

        webTestClient.get()
                .uri(uriBuilder ->
                        uriBuilder.path("/customer")
                                .queryParam("page", 1)
                                .queryParam("size", 1)
                                .build())
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.customers[0].id").isEqualTo("as64a654da4");
    }

}
