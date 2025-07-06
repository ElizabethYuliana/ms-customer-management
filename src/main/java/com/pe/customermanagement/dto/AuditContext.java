package com.pe.customermanagement.dto;

import com.pe.customermanagement.entity.Customer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter @Setter
public class AuditContext<T, R> {
    private String traceParent;
    private T request;
    private R response;
    private Customer customer;
    private Map<String, String> headers;
    private String uri;
    private String method;
    private String status;

}
