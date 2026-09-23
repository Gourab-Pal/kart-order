package com.kart.order.kafka.event;

import java.util.UUID;

public record ProductCreatedPayload(
        UUID productId
) {
}
