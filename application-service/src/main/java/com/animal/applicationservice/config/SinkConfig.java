package com.animal.applicationservice.config;

import com.animal.applicationservice.controller.model.Notification;
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
    public Sinks.Many<ServerSentEvent<Notification>> sink(){
        return Sinks.many().replay().all();
    }

    @Bean
    public Flux<ServerSentEvent<Notification>> broadcast(Sinks.Many<ServerSentEvent<Notification>> sink){
        return sink.asFlux();
    }
}
