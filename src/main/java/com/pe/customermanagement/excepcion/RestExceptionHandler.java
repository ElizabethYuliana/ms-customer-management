package com.pe.customermanagement.excepcion;

import com.pe.customermanagement.dto.ErrorResponse;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleMissing
    +
    RequestHeaderException(ServerWebInputException ex) {
        ErrorResponse errorResponse = new ErrorResponse("400", "Missing request header",
                String.format("Header '%s' is missing", ex.getHeaders()));
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
    }

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

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(WebExchangeBindException ex) {
        String errorMsg = ex.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce((m1, m2) -> m1 + "; " + m2)
                .orElse("Validation error occurred");
        ErrorResponse errorResponse = new ErrorResponse("400", "Validation error", errorMsg);
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse));
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
