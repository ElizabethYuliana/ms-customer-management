package com.pe.customermanagement.dto;

import lombok.*;

import java.util.Map;

/**
 * AuditContext is a data transfer object that encapsulates the context of an audit event.
 * It includes details about the request, response, error, headers, URI, method, and status.
 *
 * @author Elizabeth Valdez
 * @version 1.0
 */
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class AuditContext {
    private String               traceParent;
    private CustomerRequest      request;
    private CustomerResponse     response;
    private CustomerResponsePage responsePage;
    private ErrorResponse        error;
    private Map<String, String>  headers;
    private String               uri;
    private String               method;
    private String               status;

}
