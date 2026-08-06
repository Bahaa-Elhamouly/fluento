package com.fluento.features.subscription.repository;

import com.fluento.features.subscription.entity.PaymentEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaymentEventRepository
        extends JpaRepository<PaymentEvent, UUID> {
}
