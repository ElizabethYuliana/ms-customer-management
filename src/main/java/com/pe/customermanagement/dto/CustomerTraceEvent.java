package com.pe.customermanagement.dto;


public record CustomerTraceEvent(
     String analyticsTraceSource,
     String applicationId,
     String channelOperationNumber,
     String consumerId,
     String currentDate,
     String customerId,
     String region,
     String statusCode,
     long timestamp,
     String traceId,
     String inbound,
     String outbound,
     String transactionCode) {
}
