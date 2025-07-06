package com.pe.customermanagement.dto;

import com.pe.customermanagement.enums.StatusEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

public record CustomerRequest(
        @NotBlank(message = "Name cannot be empty or null")
        String name,
        @NotBlank(message = "FirstLastName cannot be empty or null")
        String firstLastName,
        @NotNull(message = "SecondLastName cannot be empty or null")
        String secondLastName,
        @Pattern(regexp = "^(Active|Inactive)$", message = "Status must be either 'Active' or 'Inactive'")
        String status) {
}
