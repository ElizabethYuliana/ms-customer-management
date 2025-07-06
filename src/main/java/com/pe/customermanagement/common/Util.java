package com.pe.customermanagement.common;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Util {

    public static String convertObjectToJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON string: " + e.getMessage(), e);
        }
    }

    private Util() {
    }
}
