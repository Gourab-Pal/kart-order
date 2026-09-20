package com.kart.order.cart.dto;

import com.kart.order.cart.entity.CartEntity;
import com.kart.order.cart.entity.CartItemEntity;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record CartSummaryResponse(
        UUID cartId,
        String status,
        List<CartItemSummaryResponse> items,
        OffsetDateTime updatedAt
) {
    public static CartSummaryResponse from(CartEntity cart, List<CartItemEntity> cartItems) {
        List<CartItemSummaryResponse> items = new ArrayList<>();
        for (CartItemEntity cartItem : cartItems) {
            items.add(CartItemSummaryResponse.from(cartItem));
        }
        return new CartSummaryResponse(
                cart.getId(),
                cart.getStatus(),
                items,
                cart.getUpdatedAt()
        );
    }
}
