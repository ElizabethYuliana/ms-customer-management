package com.pe.customermanagement.mapper;

import com.pe.customermanagement.entity.Customer;
import com.pe.customermanagement.dto.*;
import com.pe.customermanagement.enums.StatusEnum;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.pe.customermanagement.common.Constant.SPACE;
import static org.apache.logging.log4j.util.Strings.EMPTY;

/**
 * CustomerMapper is responsible for mapping between CustomerRequest, Customer, and CustomerResponse objects.
 * It provides methods to convert a request into a Customer entity and to convert a Customer entity into a response.
 *
 * @author Elizabeth Valdez
 * @version 1.0
 */
@Component
public class CustomerMapper {

    public Customer fromRequest(CustomerRequest request) {
        return Customer.builder()
                .name(request.name())
                .firstLastName(request.firstLastName())
                .secondLastName(request.secondLastName())
                .creationDate(LocalDateTime.now())
                .status(Objects.nonNull(request.status()) ? StatusEnum.fromValue(request.status()).getValue() :
                        StatusEnum.ACTIVE.getValue())
                .build();
    }

    public CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                Optional.ofNullable(customer.getName()).orElse(EMPTY).concat(SPACE)
                .concat(Optional.ofNullable(customer.getFirstLastName()).orElse(EMPTY)).concat(SPACE)
                .concat(Optional.ofNullable(customer.getSecondLastName()).orElse(EMPTY)));
    }

    public CustomerResponsePage toCustomerResponsePage(PageImpl<CustomerResponse> customerResponses)  {
        return new CustomerResponsePage(
                customerResponses.getTotalElements(),
                customerResponses.getTotalPages(),
                customerResponses.getNumber(),
                customerResponses.getSize(),
                customerResponses.getContent());
    }

}
