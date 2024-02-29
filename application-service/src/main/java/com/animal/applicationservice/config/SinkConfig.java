package com.animal.applicationservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerSentEvent;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.UUID;

@Configuration
public class SinkConfig {

    @Bean
    public Sinks.Many<ServerSentEvent<String>> sink(){
        return Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    }

    @Bean
    public Flux<ServerSentEvent<String>> broadcast(Sinks.Many<ServerSentEvent<String>> sink){
        return sink.asFlux();
    }
}
