package com.example.application.orders;

import com.example.domain.orders.OrderPlacedDomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Reference domain-event handler: runs AFTER the placing transaction has committed
 * (at-most-once semantics — a failure here is logged, the order stays placed).
 * Replace the log line with a real reaction (email, projection, ...) as needs arise.
 */
@Component
class OrderPlacedListener {

    private static final Logger log = LoggerFactory.getLogger(OrderPlacedListener.class);

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void on(OrderPlacedDomainEvent event) {
        log.info("Order {} placed for customer {} — total {}",
            event.orderId(), event.customerId(), event.total());
    }
}
