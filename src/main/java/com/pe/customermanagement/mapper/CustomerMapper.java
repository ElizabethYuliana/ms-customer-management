package com.pe.customermanagement.mapper;

import com.pe.customermanagement.entity.Customer;
import com.pe.customermanagement.model.*;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.pe.customermanagement.common.Constant.APPLICATION;
import static com.pe.customermanagement.common.Constant.SPACE;

@Component
public class CustomerMapper {

    public Customer fromRequest(CustomerRequest customerRequest) {
        return Customer.builder()
                .name(customerRequest.name())
                .firstLastName(customerRequest.firstLastName())
                .secondLastName(customerRequest.secondLastName())
                .creationDate(LocalDateTime.now())
                .status("Active")
                .build();
    }

    public CreateCustomerResponse toResponse(Customer customer) {
        return new CreateCustomerResponse("0000", true);
    }

    public CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName().concat(SPACE)
                        .concat(customer.getFirstLastName()).concat(SPACE)
                        .concat(customer.getSecondLastName()));
    }



}
