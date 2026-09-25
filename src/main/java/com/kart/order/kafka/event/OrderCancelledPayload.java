package com.kart.order.kafka.event;

import java.util.UUID;

public record OrderCancelledPayload(
        UUID orderId
) {
}
