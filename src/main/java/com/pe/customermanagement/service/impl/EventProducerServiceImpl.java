package com.pe.customermanagement.service.impl;

import com.azure.messaging.eventhubs.EventData;
import com.azure.messaging.eventhubs.EventHubProducerAsyncClient;
import com.pe.customermanagement.service.EventProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.util.List;

import static com.pe.customermanagement.common.Constant.*;
import static com.pe.customermanagement.common.LogConstant.EVENT_HUB;

/**
 * EventProducerServiceImpl is an implementation of the EventProducerService that sends events to Azure Event Hub.
 * It uses the EventHubProducerAsyncClient to send messages asynchronously.
 *
 * @author Elizabeth Valdez
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EventProducerServiceImpl implements EventProducerService {

    private final EventHubProducerAsyncClient producer;

    /**
     * Sends an event message to the Azure Event Hub.
     *
     * @param message The message to be sent as an event.
     * @return A Mono that completes when the event is successfully sent.
     */
    @Override
    public Mono<Void> sendEvent(String message) {
        EventData eventData = new EventData(message);
        return producer.send(List.of(eventData))
                .doOnSubscribe(sub -> log.info(EVENT_HUB, message))
                .doOnError(e -> log.error(EVENT_HUB_NOK, e))
                .doOnSuccess(unused -> log.info(EVENT_HUB_OK));
    }
}
