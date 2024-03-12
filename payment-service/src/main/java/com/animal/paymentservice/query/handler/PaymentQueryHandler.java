package com.animal.paymentservice.query.handler;

import com.animal.common.query.FetchPaymentIntentIdByIdQuery;
import com.animal.paymentservice.data.model.Payment;
import com.animal.paymentservice.data.repository.PaymentRepository;
import lombok.extern.slf4j.Slf4j;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class PaymentQueryHandler {

    @Autowired
    private PaymentRepository paymentRepository;

    @QueryHandler
    public Mono<String> handle(FetchPaymentIntentIdByIdQuery query) {
        return paymentRepository
                .findById(query.getPaymentId())
                .map(Payment::getPaymentIntentId);
    }
}
