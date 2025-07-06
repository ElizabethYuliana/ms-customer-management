package com.pe.customermanagement.enums;

import com.pe.customermanagement.excepcion.BadRequestException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum StatusEnum {
    ACTIVE("Active"),
    INACTIVE("Inactive");

    private final String value;

    StatusEnum(String value) {
        this.value = value;
    }

    public static StatusEnum fromValue(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BadRequestException("Status cannot be null or empty");
        }

        return Arrays.stream(StatusEnum.values())
                .filter(status -> status.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new BadRequestException(
                        String.format("Invalid status: '%s'. Allowed values are: %s",
                                value,
                                Arrays.toString(StatusEnum.values()))
                ));
    }



}
