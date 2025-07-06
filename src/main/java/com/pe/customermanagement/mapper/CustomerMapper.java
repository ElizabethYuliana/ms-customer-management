package com.pe.customermanagement.mapper;

import com.pe.customermanagement.entity.Customer;
import com.pe.customermanagement.dto.*;
import com.pe.customermanagement.enums.StatusEnum;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

import static com.pe.customermanagement.common.Constant.SPACE;

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

    public CreateCustomerResponse toResponse(Customer customer) {
        return new CreateCustomerResponse("0000", true);
    }

    public CustomerResponse toCustomerResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                Optional.ofNullable(customer.getName()).orElse(Strings.EMPTY).concat(SPACE)
                .concat(Optional.ofNullable(customer.getFirstLastName()).orElse(Strings.EMPTY)).concat(SPACE)
                .concat(Optional.ofNullable(customer.getSecondLastName()).orElse(Strings.EMPTY)));
    }

    public CustomerResponsePage toCustomerResponsePage(PageImpl<CustomerResponse> customerResponses)  {
        return new CustomerResponsePage("0000",
                true,
                customerResponses.getTotalElements(),
                customerResponses.getTotalPages(),
                customerResponses.getNumber(),
                customerResponses.getSize(),
                customerResponses.getContent());
    }


}
