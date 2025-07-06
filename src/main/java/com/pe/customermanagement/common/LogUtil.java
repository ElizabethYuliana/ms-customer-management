package com.pe.customermanagement.common;

import com.pe.customermanagement.dto.AuditContext;
import com.pe.customermanagement.entity.Customer;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.server.ServerWebExchange;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.pe.customermanagement.common.Constant.*;

@Slf4j
public class LogUtil {

    public static <T, R> AuditContext<T, R>  buildAuditContext(T request, R response,
                                                               Customer customer, ServerWebExchange exchange) {
        AuditContext<T, R>  auditContext = new AuditContext<>();
        Map<String, String> headers = buildHeaders(exchange.getRequest().getHeaders().asSingleValueMap());
        auditContext.setTraceParent(headers.get(TRACE_PARENT));
        auditContext.setRequest(request);
        auditContext.setResponse(response);
        auditContext.setCustomer(customer);
        auditContext.setHeaders(headers);
        auditContext.setUri(exchange.getRequest().getURI().toString());
        auditContext.setMethod(exchange.getRequest().getMethod().toString());
        auditContext.setStatus(Objects.requireNonNull(exchange.getResponse().getStatusCode()).toString());
        return auditContext;
    }


    private static Map<String, String> buildHeaders(Map<String, String> headers) {
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put(CONSUMER_HEADER, headers.getOrDefault(CONSUMER_HEADER, Strings.EMPTY));
        headersMap.put(TRACE_PARENT, headers.getOrDefault(TRACE_PARENT, Strings.EMPTY));
        headersMap.put(DEVICE_TYPE, headers.getOrDefault(DEVICE_TYPE, Strings.EMPTY));
        headersMap.put(DEVICE_ID, headers.getOrDefault(DEVICE_ID, Strings.EMPTY));
        headersMap.put(CONTENT_TYPE, headers.getOrDefault(CONTENT_TYPE, Strings.EMPTY));
        return headersMap;
    }

    public static <T, R>  void buildLogging(AuditContext<T, R>  auditContext) {
        log.info(LogConstant.LOG_SUCCESS,
                auditContext.getTraceParent(),
                auditContext.getMethod(),
                auditContext.getUri(),
                auditContext.getHeaders().toString(),
                Util.convertObjectToJsonString(auditContext.getRequest()),
                auditContext.getStatus(),
                Util.convertObjectToJsonString(auditContext.getResponse()));
    }

    private LogUtil() {
    }
}
