package com.pe.customermanagement.mapper;

import com.pe.customermanagement.common.Constant;
import com.pe.customermanagement.common.TransactionCodeProperties;
import com.pe.customermanagement.common.Util;
import com.pe.customermanagement.dto.*;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static com.pe.customermanagement.common.Constant.*;
import static com.pe.customermanagement.common.Constant.CONTENT_TYPE_HEADER;
import static com.pe.customermanagement.common.Constant.DEVICE_ID_HEADER;
import static com.pe.customermanagement.common.Constant.DEVICE_TYPE_HEADER;
import static org.apache.logging.log4j.util.Strings.EMPTY;

/**
 * AuditContextMapper is responsible for mapping the incoming request and response data into an AuditContext.
 * It builds the audit context for both requests and responses, including headers, trace information, and customer data.
 *
 * @author Elizabeth Valdez
 * @version 1.0
 */
@Component
@RequiredArgsConstructor
public class AuditContextMapper {

    private final TransactionCodeProperties transactionCode;

    public AuditContext buildRequestAuditContext(CustomerRequest request, ServerWebExchange exchange) {
        return buildCommonAuditContext(exchange, request);
    }

    public AuditContext buildAuditContext(ServerWebExchange exchange) {
        return buildCommonAuditContext(exchange, null);
    }

    private AuditContext buildCommonAuditContext(ServerWebExchange exchange, CustomerRequest request) {
        var req = exchange.getRequest();
        var res = exchange.getResponse();
        Map<String, String> headers = buildHeaders(req.getHeaders().asSingleValueMap());

        return AuditContext.builder()
                .traceParent(headers.get(TRACE_PARENT_HEADER))
                .headers(headers)
                .request(request)
                .uri(req.getURI().toString())
                .method(String.valueOf(req.getMethod()))
                .status(String.valueOf(Objects.requireNonNull(res.getStatusCode())))
                .build();
    }

    private Map<String, String> buildHeaders(Map<String, String> headers) {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(CONSUMER_HEADER, headers.getOrDefault(CONSUMER_HEADER, Strings.EMPTY));
        headersMap.put(TRACE_PARENT_HEADER, headers.getOrDefault(TRACE_PARENT_HEADER, Strings.EMPTY));
        headersMap.put(DEVICE_TYPE_HEADER, headers.getOrDefault(DEVICE_TYPE_HEADER, Strings.EMPTY));
        headersMap.put(DEVICE_ID_HEADER, headers.getOrDefault(DEVICE_ID_HEADER, Strings.EMPTY));
        headersMap.put(CONTENT_TYPE_HEADER, headers.getOrDefault(CONTENT_TYPE_HEADER, Strings.EMPTY));
        return headersMap;
    }

    public String buildCustomerTraceEvent(AuditContext context, String status) {
        LocalDateTime now = LocalDateTime.now();
        String consumerHeader = context.getHeaders().get(Constant.CONSUMER_HEADER);
        CustomerTraceEvent customerTraceEvent = CustomerTraceEvent.builder()
                .analyticsTraceSource(Constant.APPLICATION.concat(consumerHeader))
                .applicationId(consumerHeader)
                .channelOperationNumber(Util.convertDateToEpochMilli(now).toString())
                .consumerId(consumerHeader)
                .currentDate(now.toString())
                .customerId(getCustomerId(context))
                .region(Constant.REGION)
                .statusCode(status)
                .timestamp(Util.convertDateToEpochMilli(now))
                .traceId(context.getTraceParent())
                .inbound(getInBound(context))
                .outbound(getOutBound(context))
                .transactionCode(transactionCode.getTransactionCode(context.getMethod()))
                .build();
        return Util.convertObjectToJsonString(customerTraceEvent);
    }

    private String getCustomerId(AuditContext context) {
        return Optional.ofNullable(context.getResponse())
                .map(CustomerResponse::id)
                .orElse(EMPTY);
    }

    private String getInBound(AuditContext context) {
        return Optional.ofNullable(context.getRequest())
                .map(Util::convertObjectToJsonString)
                .orElse(EMPTY);
    }

    private String getOutBoundErrorResponse(AuditContext context) {
        return Optional.ofNullable(context.getError())
                .map(Util::convertObjectToJsonString)
                .orElse(EMPTY);
    }

    private String getOutBound(AuditContext context) {
        return Optional.ofNullable(context.getResponse())
                .map(Util::convertObjectToJsonString)
                .orElseGet(() -> Optional.ofNullable(context.getResponsePage())
                        .map(Util::convertObjectToJsonString)
                        .orElseGet(() -> getOutBoundErrorResponse(context)));
    }

    public AuditContext buildCustomerResponse(CustomerResponse response, AuditContext context) {
        context.setResponse(response);
        return context;
    }

    public AuditContext buildCustomerResponsePage(CustomerResponsePage response, AuditContext context) {
        context.setResponsePage(response);
        return context;
    }

    public AuditContext buildErrorAuditContext(ErrorResponse error, ServerWebExchange exchange) {
        AuditContext auditContext = buildAuditContext(exchange);
        auditContext.setError(error);
        return auditContext;
    }
}
