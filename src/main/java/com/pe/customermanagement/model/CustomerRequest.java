package com.pe.customermanagement.model;

import java.time.LocalDateTime;

public record CustomerRequest(String name,
                              String firstLastName,
                              String secondLastName,
                              LocalDateTime creationDate,
                              String status) {
}
