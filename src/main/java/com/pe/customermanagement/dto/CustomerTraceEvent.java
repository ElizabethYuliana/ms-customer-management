package com.pe.customermanagement.dto;


import lombok.*;


/**
 * CustomerTraceEvent is a data transfer object that encapsulates the details of a customer trace event.
 * It includes fields for analytics trace.
 *
 * @author Elizabeth Valdez
 * @version 1.0
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomerTraceEvent {
    private String analyticsTraceSource;
    private String applicationId;
    private String channelOperationNumber;
    private String consumerId;
    private String currentDate;
    private String customerId;
    private String region;
    private String statusCode;
    private long   timestamp;
    private String traceId;
    private String inbound;
    private String outbound;
    private String transactionCode;
}
