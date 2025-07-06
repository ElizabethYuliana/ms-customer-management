package com.pe.customermanagement.dto;

public record ErrorResponse(String errorCode,
                            String errorMessage,
                            String moreInfo) {
}
