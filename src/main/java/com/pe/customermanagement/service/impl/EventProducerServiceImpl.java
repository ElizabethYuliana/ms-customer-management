package com.pe.customermanagement.service.impl;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubProducerAsyncClient;
import com.pe.customermanagement.common.*;
import com.pe.customermanagement.dto.*;
import com.pe.customermanagement.service.EventProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.pe.customermanagement.common.Constant.*;
import static com.pe.customermanagement.common.LogConstant.EVENT_HUB;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventProducerServiceImpl implements EventProducerService {

    private final EventHubProducerAsyncClient producer;
    private final TransactionCodeProperties   transactionCodeProperties;

    @Override
    public <T, R> void sendAuditMessage(AuditContext<T, R> auditContext) {
        String message = buildCustomerTraceEvent(auditContext);
        EventData eventData = new EventData(message);
        producer.send(List.of(eventData)).subscribe();
        log.info(EVENT_HUB, message);
    }

    public <T, R, V> String buildCustomerTraceEvent(AuditContext<T, R> auditContext) {
        LocalDateTime now = LocalDateTime.now();
        String consumerHeader = auditContext.getHeaders().get(Constant.CONSUMER_HEADER);
        CustomerTraceEvent customerTraceEvent = new CustomerTraceEvent(
                APPLICATION.concat(consumerHeader),
                consumerHeader,
                Util.convertDateToEpochMilli(now).toString(),
                consumerHeader,
                now.toString(),
                auditContext.getCustomer().getId(),
                REGION,
                "0000",
                Util.convertDateToEpochMilli(now),
                auditContext.getTraceParent(),
                Util.convertObjectToJsonString(auditContext.getRequest()),
                Util.convertObjectToJsonString(auditContext.getResponse()),
                transactionCodeProperties.getTransactionCode(auditContext.getMethod()));
        return Util.convertObjectToJsonString(customerTraceEvent);
    }
}
