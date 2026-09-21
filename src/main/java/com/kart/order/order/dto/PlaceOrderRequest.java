package com.kart.order.order.dto;

import java.util.UUID;

public record PlaceOrderRequest(
        UUID cartId
) {
}
