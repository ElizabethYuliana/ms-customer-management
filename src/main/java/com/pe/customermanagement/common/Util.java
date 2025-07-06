package com.pe.customermanagement.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.util.Strings;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Util {

    public static String convertObjectToJsonString(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException("Error converting object to JSON string: " + e.getMessage(), e);
        }
    }

    public static String extractHeaderName(String message) {
        Pattern pattern = java.util.regex.Pattern.compile("'(.*?)'");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return Strings.EMPTY;
    }

    public static Long convertDateToEpochMilli(LocalDateTime date) {
        if (Objects.isNull(date)) {
            return 0L;
        }
        return date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    //generar un valor random asi 1729182742782
    public static String generateRandomValue() {
        return String.valueOf(System.currentTimeMillis());
    }



    private Util() {
    }
}
