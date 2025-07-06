package com.pe.customermanagement.dto;

public record HeaderRequest(String consumerId,
                            String traceParent,
                            String deviceType,
                            String deviceId) {
}
