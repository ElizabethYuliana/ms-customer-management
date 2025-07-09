package com.pe.customermanagement.dto;

/**
 * ErrorResponse is a data transfer object that encapsulates the details of an error response.
 * It includes fields for the error code, error message, and additional information.
 *
 * @author Elizabeth Valdez
 * @version 1.0
 */
public record ErrorResponse(String errorCode,
                            String errorMessage,
                            String moreInfo) {
}
