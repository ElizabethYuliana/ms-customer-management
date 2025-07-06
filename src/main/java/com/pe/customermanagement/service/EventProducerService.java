package com.pe.customermanagement.service;

import com.pe.customermanagement.dto.AuditContext;

public interface EventProducerService {

    <T, R> void sendAuditMessage(AuditContext<T, R> auditContext);
}
