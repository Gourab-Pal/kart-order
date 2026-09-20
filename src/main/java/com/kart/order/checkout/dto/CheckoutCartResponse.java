package com.kart.order.checkout.dto;

import com.kart.order.cart.entity.CartEntity;

import java.util.List;
import java.util.UUID;

public record CheckoutCartResponse(
        UUID cartId,
        String status,
        List<CheckoutItemResponse> items,
        boolean canProceed
) {
    public static CheckoutCartResponse from(CartEntity cartEntity, List<CheckoutItemResponse> items) {
        boolean canProceed = true;
        for (CheckoutItemResponse item : items) {
            if(!item.eligible()) {
                canProceed = false;
                break;
            }
        }
        return new CheckoutCartResponse(
                cartEntity.getId(),
                cartEntity.getStatus(),
                items,
                canProceed
        );
    }
}
