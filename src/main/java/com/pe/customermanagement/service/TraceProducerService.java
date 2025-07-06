package com.pe.customermanagement.service;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubProducerAsyncClient;
import com.pe.customermanagement.common.Util;
import com.pe.customermanagement.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.pe.customermanagement.common.Constant.APPLICATION;
import static com.pe.customermanagement.common.Constant.SPACE;

@Service
@RequiredArgsConstructor
@Slf4j
public class TraceProducerService {

    private final EventHubProducerAsyncClient producer;

    public void sendMessage(CustomerRequest request,
                            CreateCustomerResponse response,
                            HeaderRequest headerRequest) {
        String message = buildCustomerTraceEvent(request, response, headerRequest);
        log.info("Sending message to Event Hub: {}", message);
        EventData eventData = new EventData(message);
        producer.send(List.of(eventData))
                .subscribe();;
    }

    public String buildCustomerTraceEvent(CustomerRequest request,
                                          CreateCustomerResponse response,
                                          HeaderRequest headerRequest) {

        CustomerTraceEvent customerTraceEvent = new CustomerTraceEvent(
                String.format(APPLICATION, headerRequest.consumerId()),
                headerRequest.consumerId(),
                SPACE,
                SPACE,
                LocalDateTime.now().toString(),
                SPACE,
                "east",
                "0000",
                6546545L,
                UUID.randomUUID().toString(),
                Util.convertObjectToJsonString(request),
                Util.convertObjectToJsonString(response),
                "0001");
        return Util.convertObjectToJsonString(customerTraceEvent);
    }
}
