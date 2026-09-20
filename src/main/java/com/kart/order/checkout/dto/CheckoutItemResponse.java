package com.kart.order.checkout.dto;

import java.util.UUID;

public record CheckoutItemResponse(
        UUID productId,
        int requestedQuantity,
        int availableQuantity,
        boolean eligible
) {
    public static CheckoutItemResponse from(UUID productId,  int requestedQuantity, int availableQuantity) {
        return new  CheckoutItemResponse(
                productId,
                requestedQuantity,
                availableQuantity,
                requestedQuantity <= availableQuantity
        );
    }
}
