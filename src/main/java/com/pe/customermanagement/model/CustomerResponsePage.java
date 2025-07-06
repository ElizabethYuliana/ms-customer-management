package com.pe.customermanagement.model;

import java.util.List;

public record CustomerResponsePage(
    String statusCode,
    boolean success,
    long totalElements,
    int totalPages,
    int pageNumber,
    int pageSize,
    List<CustomerResponse> customers) {
}
