package com.animal.paymentservice.data.repository;

import com.animal.paymentservice.data.model.Payment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends ReactiveCrudRepository<Payment, String> {
}
