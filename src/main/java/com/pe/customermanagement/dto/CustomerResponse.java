package com.pe.customermanagement.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record CustomerResponse(String id,
                               String fullName) {
}
