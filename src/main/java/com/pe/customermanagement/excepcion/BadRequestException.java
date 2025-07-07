package com.pe.customermanagement.excepcion;

import lombok.Setter;

@Setter
public class BadRequestException extends RuntimeException {


    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
