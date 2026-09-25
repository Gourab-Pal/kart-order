package com.kart.order.kafka.event;

import java.util.UUID;

public record OrderConfirmedPayload(
        UUID orderId,
        UUID cartId
) {
}
