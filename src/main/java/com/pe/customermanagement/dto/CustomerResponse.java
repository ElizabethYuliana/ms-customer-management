package com.pe.customermanagement.dto;

/**
 * CustomerResponse is a data transfer object that encapsulates the details of a customer response.
 * It includes fields for the customer's ID and full name.
 *
 * @author Elizabeth Valdez
 * @version 1.0
 */
public record CustomerResponse(String id,
                               String fullName) {
}
