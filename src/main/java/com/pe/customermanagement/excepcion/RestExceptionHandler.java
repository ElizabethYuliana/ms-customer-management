package com.pe.customermanagement.excepcion;

import com.pe.customermanagement.common.LogUtil;
import com.pe.customermanagement.dto.AuditContext;
import com.pe.customermanagement.dto.ErrorResponse;
import com.pe.customermanagement.mapper.AuditContextMapper;
import com.pe.customermanagement.service.EventProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static com.pe.customermanagement.common.Constant.*;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class RestExceptionHandler {

    private final EventProducerService eventProducerService;
    private final AuditContextMapper   customerMapper;


    @ExceptionHandler(BadRequestException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleBadRequestException(BadRequestException ex,
                                                                         ServerWebExchange request) {
        ErrorResponse response = new ErrorResponse(ERROR_CODE, BAD_REQUEST_MESSAGE, ex.getMessage());
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, response, ex);
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handleGeneralException(Exception ex, ServerWebExchange request) {
        ErrorResponse response = new ErrorResponse(ERROR_CODE, UNEXPECTED_ERROR_MESSAGE, ex.getMessage());
        return buildErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, response, ex);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleValidationException(WebExchangeBindException ex,
                                                                         ServerWebExchange request) {
        String errorMsg = ex.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .filter(Objects::nonNull)
                .reduce((m1, m2) -> m1.concat(COMMA).concat(m2))
                .orElse(UNEXPECTED_ERROR_MESSAGE);
        ErrorResponse response = new ErrorResponse(ERROR_CODE, BAD_REQUEST_MESSAGE, errorMsg);
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, response, ex);
    }

    @ExceptionHandler(NotFoundException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleNotFoundException(NotFoundException ex,
                                                                       ServerWebExchange request) {
        ErrorResponse response = new ErrorResponse(ERROR_CODE, RESOURCE_NOT_FOUND_MSG, ex.getMessage());
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, response, ex);
    }

    @ExceptionHandler(InternalErrorException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleInternalErrorException(InternalErrorException ex,
                                                                            ServerWebExchange request) {
        ErrorResponse response = new ErrorResponse(ERROR_CODE, INTERNAL_ERROR_MESSAGE, ex.getMessage());
        return buildErrorResponse(request, HttpStatus.INTERNAL_SERVER_ERROR, response, ex);
    }

    private Mono<ResponseEntity<ErrorResponse>> buildErrorResponse(ServerWebExchange request,
                                                                   HttpStatus status,
                                                                   ErrorResponse response,
                                                                   Exception ex) {
        request.getResponse().setStatusCode(status);
        AuditContext context = customerMapper.buildErrorAuditContext(response, request);
        return eventProducerService.sendEvent(customerMapper.buildCustomerTraceEvent(context, ERROR_CODE))
                .doOnSuccess(v -> LogUtil.buildLoggingError(context, ex))
                .thenReturn(ResponseEntity.status(status).body(response));
    }
}
