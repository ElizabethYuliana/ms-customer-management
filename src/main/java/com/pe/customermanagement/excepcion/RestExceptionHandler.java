package com.pe.customermanagement.excepcion;

import com.pe.customermanagement.model.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleBadRequestException(BadRequestException ex) {
        ErrorResponse errorResponse = new ErrorResponse("999", ex.getMessage(), "Please check the request parameters.");
        return Mono.just(ResponseEntity.badRequest().body(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneralException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse("500", "An unexpected error occurred", ex.getMessage());
        return Mono.just(ResponseEntity.status(500).body(errorResponse));
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNotFoundException(NotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse("404", "Resource not found", ex.getMessage());
        return Mono.just(ResponseEntity.status(404).body(errorResponse));
    }

    @ExceptionHandler(InternalErrorException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInternalErrorException(InternalErrorException ex) {
        ErrorResponse errorResponse = new ErrorResponse("500", "Internal server error", ex.getMessage());
        return Mono.just(ResponseEntity.status(500).body(errorResponse));
    }
}
