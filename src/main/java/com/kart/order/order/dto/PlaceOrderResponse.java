package com.kart.order.order.dto;

import com.kart.order.order.entity.OrderEntity;

import java.util.UUID;

public record PlaceOrderResponse(
        UUID orderId,
        UUID cartId,
        String message
) {
    public static PlaceOrderResponse from(OrderEntity order) {
        return new PlaceOrderResponse(
                order.getId(),
                order.getCartId(),
                "Order placed successfully"
        );
    }
}
