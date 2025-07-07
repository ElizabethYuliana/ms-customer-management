package com.pe.customermanagement.dto;

import java.util.List;

/**
 * CustomerResponsePage is a data transfer object that encapsulates a paginated response of customer data.
 * It includes the total number of elements, total pages, current page number, page size, and a list of customer.
 *
 * @author Elizabeth Valdez
 * @version 1.0
 */
public record CustomerResponsePage(
    long totalElements,
    int totalPages,
    int pageNumber,
    int pageSize,
    List<CustomerResponse> customers) {
}
