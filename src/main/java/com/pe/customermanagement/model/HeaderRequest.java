package com.pe.customermanagement.model;

import org.springframework.web.bind.annotation.RequestHeader;

public record HeaderRequest(String consumerId,
                            String traceParent,
                            String deviceType,
                            String deviceId) {
}
