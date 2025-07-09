package com.pe.customermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

/**
 * CustomerRequest is a data transfer object that encapsulates the details of a customer request.
 * It includes fields for the customer's name, first last name, second last name, and status.
 *
 * @author Elizabeth Valdez
 * @version 1.0
 */
public record CustomerRequest(
        @NotBlank(message = "Name cannot be empty or null")
        String name,
        @NotBlank(message = "FirstLastName cannot be empty or null")
        String firstLastName,
        @NotNull(message = "SecondLastName cannot be null")
        String secondLastName,
        @Pattern(regexp = "^(Active|Inactive)$", message = "Status must be either 'Active' or 'Inactive'")
        String status) {
}
