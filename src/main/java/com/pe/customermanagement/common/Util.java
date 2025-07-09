package com.pe.customermanagement.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pe.customermanagement.excepcion.InternalErrorException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;

public class Util {

    public static String convertObjectToJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new InternalErrorException("Error converting object to JSON string: " + e.getMessage());
        }
    }

    public static Long convertDateToEpochMilli(LocalDateTime date) {
        if (Objects.isNull(date)) {
            return 0L;
        }
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    private Util() {
    }
}
