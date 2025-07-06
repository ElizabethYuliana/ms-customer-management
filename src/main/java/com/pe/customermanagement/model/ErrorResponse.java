package com.pe.customermanagement.model;

public record ErrorResponse(String errorCode,
                            String errorMessage,
                            String moreInfo) {
}
