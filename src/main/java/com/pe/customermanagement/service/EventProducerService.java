package com.pe.customermanagement.service;


import reactor.core.publisher.Mono;

public interface EventProducerService {

    Mono<Void> sendEvent(String message);
}
