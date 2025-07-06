package com.pe.customermanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CustomerRequest(@NotBlank(message = "Name cannot be empty or null") String name,
                              @NotBlank(message = "FirstLastName cannot be empty or null") String firstLastName,
                              @NotNull(message = "SecondLastName cannot be empty or null") String secondLastName,
                              LocalDateTime creationDate,
                              String status) {
}
